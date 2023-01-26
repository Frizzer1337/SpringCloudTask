package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.Payment
import com.mongodb.client.result.UpdateResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CreditRepository {

    fun pay(payment: Payment): Mono<Credit>

    fun checkIfCreditPayed(credit: Credit): Mono<Boolean>

    fun sendPenalty(): Mono<Boolean>

    fun checkCreditToSendCollector(): Flux<UpdateResult>

    fun findCreditToSendCollector(): Flux<Credit>

    fun markCreditSendToCollector(): Mono<Boolean>

    fun checkCreditToWarn(): Flux<Credit>

}