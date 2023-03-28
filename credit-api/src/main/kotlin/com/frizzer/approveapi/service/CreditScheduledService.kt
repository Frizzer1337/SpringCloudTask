package com.frizzer.approveapi.service

import com.frizzer.approveapi.repository.CreditRepository
import com.frizzer.contractapi.entity.credit.Credit
import com.frizzer.contractapi.entity.credit.CreditPayedEvent
import com.frizzer.contractapi.entity.credit.CreditStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import java.time.LocalDateTime

@Service
class CreditScheduledService(
    private val creditRepository: CreditRepository,
    private val kafkaCreditPayedTemplate: ReactiveKafkaProducerTemplate<String, CreditPayedEvent>
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(CreditService::class.java)
    }

    @Value(value = "\${credit.penalty.amount}")
    private val standardPenalty = 50

    @Value(value = "\${credit.penalty.days}")
    private val amountOfDays = 1L

    @Value(value = "\${credit.penalty.warn}")
    private val bigPenaltyMultiplier = 0.3

    @Value(value = "\${credit.penalty.collector}")
    private val creditNeedCollectorMultiplier = 0.5

    @Value(value = "\${kafka.topic.payed}")
    private val creditPayedTopic = ""

    private val paymentDelayForPenalty = LocalDateTime.now().minusDays(amountOfDays).toString()

    private val emptyBalance = 0

    @Scheduled(fixedDelay = 10000)
    fun changeStatusIfCreditPayed(): Disposable {
        return creditRepository
            .findCreditsByCreditBalanceEqualsAndCreditStatusEquals(
                emptyBalance, CreditStatus.APPROVED
            )
            .flatMap { credit ->
                credit.creditStatus = CreditStatus.CREDIT_PAYED
                creditRepository.save(credit)
            }
            .flatMap { credit -> sendCreditPayedEvent(credit) }
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    fun sendPenalty(): Disposable {
        log.info("Credit's were checked")
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(
                paymentDelayForPenalty,
                CreditStatus.APPROVED
            )
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance + standardPenalty
                credit.penalty = credit.penalty + standardPenalty
                creditRepository.save(credit)
            }
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    fun changeStatusForBigPenalty(): Disposable {
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(
                paymentDelayForPenalty,
                CreditStatus.APPROVED
            )
            .filter { credit -> credit.penalty > credit.creditBalance * creditNeedCollectorMultiplier }
            .flatMap { credit ->
                credit.creditStatus = CreditStatus.NEED_COLLECTOR
                creditRepository.save(credit)
            }
            .subscribe()
    }

    private fun sendCreditPayedEvent(credit: Credit): Mono<SenderResult<Void>> {
        return kafkaCreditPayedTemplate.send(
            creditPayedTopic,
            CreditPayedEvent(credit.id, credit.creditStatus)
        )
            .doOnSuccess { result ->
                log.info(
                    "Credit payed event sent {} offset: {}",
                    CreditPayedEvent(credit.id, credit.creditStatus),
                    result.recordMetadata().offset()
                )
            }
    }

    @Scheduled(fixedDelay = 10000)
    fun sendWarnForBigPenalty(): Disposable {
        return creditRepository
            .findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(
                paymentDelayForPenalty,
                CreditStatus.APPROVED
            )
            .filter { credit -> credit.penalty > credit.creditBalance * bigPenaltyMultiplier }
            .doOnNext { x -> log.warn("Credit have big penalty {}", x) }
            .subscribe()
    }

}