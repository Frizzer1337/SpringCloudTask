package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.Payment
import reactor.core.publisher.Mono

interface PaymentRepository{

    fun save(payment: Payment): Mono<Boolean>
}