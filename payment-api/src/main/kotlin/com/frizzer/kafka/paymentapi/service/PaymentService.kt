package com.frizzer.kafka.paymentapi.service

import com.frizzer.kafka.paymentapi.repository.PaymentRepository
import com.frizzer.contractapi.entity.Payment
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun save(payment: Payment): Mono<Payment> {
        return paymentRepository.save(payment)
    }
}