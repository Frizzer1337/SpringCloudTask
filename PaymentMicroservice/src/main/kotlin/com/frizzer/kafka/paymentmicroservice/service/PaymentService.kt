package com.frizzer.kafka.paymentmicroservice.service

import com.frizzer.kafka.paymentmicroservice.repository.PaymentRepository
import com.frizzer.kafkaapi.entity.Payment
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun save(payment: Payment): Mono<Payment> {
        return paymentRepository.save(payment)
    }
}