package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.credit.Credit
import com.frizzer.contractapi.entity.payment.Payment
import com.frizzer.contractapi.entity.payment.PaymentEvent
import com.frizzer.kafka.paymentapi.repository.CreditRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import java.time.LocalDateTime


@Service
open class CreditService(
    private val creditRepository: CreditRepository,
    private val paymentService: PaymentService,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, PaymentEvent>,
) {
    companion object {
        private val log : Logger = LoggerFactory.getLogger(CreditService::class.java)
    }

    private fun pay(payment: Payment): Mono<Credit> {
        return creditRepository.findCreditById(payment.creditId)
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance - payment.payment
                credit.penalty = credit.penalty - payment.payment
                credit.lastPaymentDate = LocalDateTime.now().toString()
                creditRepository.save(credit)
            }
            .flatMap { credit -> sendPaymentEvent(credit).thenReturn(credit) }
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
    open fun payAndSavePayment(payment: Payment): Mono<Credit> {


        return paymentService.save(payment).then(pay(payment))
    }

}