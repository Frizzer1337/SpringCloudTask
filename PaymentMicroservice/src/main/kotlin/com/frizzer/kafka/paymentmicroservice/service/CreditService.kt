package com.frizzer.kafka.paymentmicroservice.service

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.Payment
import reactor.core.Disposable
import reactor.core.publisher.Mono

interface CreditService {

    fun pay(payment: Payment): Mono<Credit>

    fun payAndSavePayment(payment: Payment): Mono<Credit>

    fun sendPenalty(): Disposable

    fun changeStatusForBigPenalty(): Disposable

    fun sendToCollectorsForBigPenalty(): Disposable

    fun sendWarnForBigPenalty(): Disposable

}