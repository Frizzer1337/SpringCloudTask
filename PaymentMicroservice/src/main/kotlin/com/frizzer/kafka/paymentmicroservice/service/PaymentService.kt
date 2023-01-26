package com.frizzer.kafka.paymentmicroservice.service

import com.frizzer.kafkaapi.entity.Payment
import reactor.core.publisher.Mono

interface PaymentService {

    fun save(payment: Payment): Mono<Boolean>

}