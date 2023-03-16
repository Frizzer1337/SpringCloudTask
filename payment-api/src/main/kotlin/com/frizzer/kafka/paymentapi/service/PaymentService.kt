package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.payment.Payment
import com.frizzer.contractapi.entity.payment.PaymentEvent
import com.frizzer.contractapi.entity.payment.PaymentStatus
import com.frizzer.kafka.paymentapi.repository.PaymentRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult


@Service
open class PaymentService(
    private val creditService: CreditService,
    private val paymentRepository: PaymentRepository,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, PaymentEvent>,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(PaymentService::class.java)
    }

    private fun sendPaymentEvent(payment: Payment): Mono<SenderResult<Void>> {
        return kafkaTemplate.send("CREDIT_PAYMENT", PaymentEvent(payment.id, payment.status))
            .doOnSuccess { result ->
                log.info(
                    "Credit payment event sent {} offset: {}",
                    PaymentEvent(payment.id, payment.status),
                    result.recordMetadata()
                )
            }
    }

    open fun pay(payment: Payment): Mono<Payment> {

        return paymentRepository.save(payment)
            .then(creditService.paySagaInCreditApi(payment))
            .doOnSuccess { payment.status = PaymentStatus.APPROVED }
            .doOnError { payment.status = PaymentStatus.CANCELED }
            .then(paymentRepository.save(payment))
            .flatMap { sendPaymentEvent(it).thenReturn(it) }
    }

}