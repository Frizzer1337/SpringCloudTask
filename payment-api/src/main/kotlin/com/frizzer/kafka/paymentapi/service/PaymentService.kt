package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.payment.*
import com.frizzer.kafka.paymentapi.client.CreditClient
import com.frizzer.kafka.paymentapi.repository.PaymentRepository
import feign.FeignException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult


@Service
open class PaymentService(
    private val creditClient: CreditClient,
    private val paymentRepository: PaymentRepository,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, PaymentEvent>,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(PaymentService::class.java)
    }

    @Value(value = "\${kafka.topic.payment}")
    private val creditPaymentTopic: String = ""

    @Transactional(noRollbackFor = [FeignException::class])
    open fun pay(paymentDto: PaymentDto): Mono<PaymentDto> {
        return creditClient.pay(paymentDto)
            .doOnSuccess { paymentDto.status = PaymentStatus.APPROVED }
            .doOnError { error ->
                log.error("Payment error: {}", error.message)
                paymentDto.status = PaymentStatus.CANCELED
            }
            .flatMap { paymentRepository.save(paymentDto.fromDto()) }
            .flatMap { sendPaymentEvent(it.toDto()).thenReturn(it.toDto()) }
    }

    private fun sendPaymentEvent(
        payment: PaymentDto
    ): Mono<SenderResult<Void>> {
        return kafkaTemplate.send(
            creditPaymentTopic,
            PaymentEvent(payment.id, payment.status)
        )
            .doOnSuccess { result ->
                log.info(
                    "Credit payment event sent {} offset: {}",
                    PaymentEvent(payment.id, payment.status),
                    result.recordMetadata()
                )
            }
    }


}