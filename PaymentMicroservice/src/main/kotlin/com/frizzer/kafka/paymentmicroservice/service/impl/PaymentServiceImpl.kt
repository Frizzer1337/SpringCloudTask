package com.frizzer.kafka.paymentmicroservice.service.impl

import com.frizzer.kafka.paymentmicroservice.repository.PaymentRepository
import com.frizzer.kafka.paymentmicroservice.service.PaymentService
import com.frizzer.kafkaapi.entity.Payment
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PaymentServiceImpl(val paymentRepository: PaymentRepository) : PaymentService {
    override fun save(payment: Payment): Mono<Boolean> {
        return paymentRepository.save(payment)
    }
}