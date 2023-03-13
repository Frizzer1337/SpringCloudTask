package com.frizzer.kafka.paymentmicroservice.service.impl

import com.frizzer.kafka.paymentmicroservice.repository.CreditRepo
import com.frizzer.kafka.paymentmicroservice.service.CollectorService
import com.frizzer.kafka.paymentmicroservice.service.CreditService
import com.frizzer.kafka.paymentmicroservice.service.PaymentService
import com.frizzer.kafkaapi.entity.*
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
    private val creditRepository: CreditRepo,
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
    }


//    private fun getSomething(x: Credit) =
//        creditRepository.checkIfCreditPayed(x).map { creditPayed ->
//            if (java.lang.Boolean.TRUE == creditPayed) {
//                kafkaCreditPayedTemplate.send("CREDIT_PAYED", CreditPayedEvent(x))
//                    .doOnSuccess { result ->
//                        log.info(
//                            "Credit payed event sent {} offset: {}",
//                            CreditPayedEvent(x),
//                            result.recordMetadata().offset()
//                        )
//                    }
//            } else {
//                kafkaTemplate.send("CREDIT_PAYMENT", PaymentEvent(x)).doOnSuccess { result ->
//                    log.info(
//                        "Credit payment event sent {} offset: {}",
//                        PaymentEvent(x),
//                        result.recordMetadata().offset()
//                    )
//                }
//            }
//        }


    @Transactional
    override fun payAndSavePayment(payment: Payment): Mono<Credit> {
        return paymentService.save(payment).then(pay(payment))
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendPenalty(): Disposable {
        val standardPenalty = 50
        log.info("Credit's were checked")
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThan(LocalDateTime.now().minusDays(1).toString())
            .doOnNext { print(it) }
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance + standardPenalty
                credit.penalty = credit.penalty + standardPenalty
                creditRepository.save(credit)
            }
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun changeStatusForBigPenalty(): Disposable {
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThan(LocalDateTime.now().minusDays(1).toString())
            .filter { credit -> credit.penalty > credit.creditBalance * 0.5 }
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
            log.info("sent {} offset: {}", credit, result.recordMetadata().offset())
        }
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendWarnForBigPenalty(): Disposable {
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThan(LocalDateTime.now().minusDays(1).toString())
            .filter { credit -> credit.penalty > credit.creditBalance * 0.3 }
            .doOnNext { x -> log.warn("Credit have big penalty {}", x) }
            .subscribe()
    }
}