package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.payment.Payment
import com.frizzer.kafka.paymentapi.repository.PaymentRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun save(payment: Payment): Mono<Payment> {
        return paymentRepository.save(payment)
    }
}