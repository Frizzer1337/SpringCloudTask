package com.frizzer.kafka.paymentapi.service.impl

import com.frizzer.contractapi.entity.*
import com.frizzer.kafka.paymentapi.repository.CreditRepository
import com.frizzer.kafka.paymentapi.service.CollectorService
import com.frizzer.kafka.paymentapi.service.CreditService
import com.frizzer.kafka.paymentapi.service.PaymentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import java.time.LocalDateTime

@Service
open class CreditServiceImpl(
    private val creditRepository: CreditRepository,
    private val paymentService: PaymentService,
    private val collectorService: CollectorService,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, PaymentEvent>,
    private val kafkaCollectorTemplate: ReactiveKafkaProducerTemplate<String, CollectorEvent>,
    private val kafkaCreditPayedTemplate: ReactiveKafkaProducerTemplate<String, CreditPayedEvent>
) : CreditService {
    private val log: Logger = LoggerFactory.getLogger(CreditServiceImpl::class.java)

    //Need help
    //Pays for credit after payment
    override fun pay(payment: Payment): Mono<Credit> {
        return creditRepository.findCreditById(payment.creditId)
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance - payment.payment
                credit.penalty = credit.penalty - payment.payment
                credit.lastPaymentDate = LocalDateTime.now().toString()
                creditRepository.save(credit)
            }
            .flatMap { credit -> sendPaymentEvent(credit).thenReturn(credit) }
    }


    private fun sendCreditPayedEvent(credit: Credit): Mono<SenderResult<Void>> {
        return kafkaCreditPayedTemplate.send("CREDIT_PAYED", CreditPayedEvent(credit))
            .doOnSuccess { result ->
                log.info(
                    "Credit payed event sent {} offset: {}",
                    CreditPayedEvent(credit),
                    result.recordMetadata().offset()
                )
            }
    }

    private fun sendPaymentEvent(credit: Credit): Mono<SenderResult<Void>> {
        return kafkaTemplate.send("CREDIT_PAYMENT", PaymentEvent(credit))
            .doOnSuccess { result ->
                log.info(
                    "Credit payment event sent {} offset: {}",
                    PaymentEvent(credit),
                    result.recordMetadata()
                )
            }
    }


    @Transactional
    override fun payAndSavePayment(payment: Payment): Mono<Credit> {
        return paymentService.save(payment).then(pay(payment))
    }

    @Scheduled(fixedDelay = 10000)
    override fun changeStatusIfCreditPayed(): Disposable {
        val emptyBalance = 0
        return creditRepository
            .findCreditsByCreditBalanceEqualsAndCreditStatusEquals(
                emptyBalance,
                CreditStatus.APPROVED
            )
            .flatMap { credit ->
                credit.creditStatus = CreditStatus.CREDIT_PAYED
                creditRepository.save(credit)
            }.flatMap { credit -> sendCreditPayedEvent(credit) }
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendPenalty(): Disposable {
        val standardPenalty = 50
        val yesterday = LocalDateTime.now().minusDays(1).toString()
        log.info("Credit's were checked")
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(yesterday, CreditStatus.APPROVED)
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance + standardPenalty
                credit.penalty = credit.penalty + standardPenalty
                creditRepository.save(credit)
            }
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun changeStatusForBigPenalty(): Disposable {
        val creditNeedCollectorMultiplier = 0.5
        val yesterday = LocalDateTime.now().minusDays(1).toString()
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(yesterday, CreditStatus.APPROVED)
            .filter { credit -> credit.penalty > credit.creditBalance * creditNeedCollectorMultiplier }
            .flatMap { credit ->
                credit.creditStatus = CreditStatus.NEED_COLLECTOR
                creditRepository.save(credit)
            }
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendToCollectorsForBigPenalty(): Disposable {
        return creditRepository.findCreditsByCreditStatus(CreditStatus.NEED_COLLECTOR)
            .flatMap { credit ->
                credit.creditStatus = CreditStatus.SEND_TO_COLLECTOR
                collectorService.save(CollectorCredit(credit)).then(creditRepository.save(credit))
            }.flatMap { credit -> sendCreditToCollectorKafka(credit) }
            .subscribe()
    }

    private fun sendCreditToCollectorKafka(credit: Credit): Mono<SenderResult<Void>> {
        return kafkaCollectorTemplate.send(
            "CREDIT_TO_COLLECTOR",
            CollectorEvent(credit.id, credit.creditStatus)
        ).doOnNext { result ->
            log.info(
                "sent {} credit to Collectors offset: {}",
                credit,
                result.recordMetadata().offset()
            )
        }
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendWarnForBigPenalty(): Disposable {
        val bigPenaltyMultiplier = 0.3
        val yesterday = LocalDateTime.now().minusDays(1).toString()
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(yesterday, CreditStatus.APPROVED)
            .filter { credit -> credit.penalty > credit.creditBalance * bigPenaltyMultiplier }
            .doOnNext { x -> log.warn("Credit have big penalty {}", x) }
            .subscribe()
    }
}