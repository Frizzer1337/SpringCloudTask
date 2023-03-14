package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.Credit
import com.frizzer.contractapi.entity.Payment
import reactor.core.Disposable
import reactor.core.publisher.Mono

interface CreditService {

    fun pay(payment: Payment): Mono<Credit>

    fun payAndSavePayment(payment: Payment): Mono<Credit>

    fun sendPenalty(): Disposable

    fun changeStatusForBigPenalty(): Disposable

    fun sendToCollectorsForBigPenalty(): Disposable

    fun sendWarnForBigPenalty(): Disposable

    fun changeStatusIfCreditPayed(): Disposable
}