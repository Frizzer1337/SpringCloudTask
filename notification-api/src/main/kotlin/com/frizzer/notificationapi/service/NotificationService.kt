package com.frizzer.notificationapi.service

import reactor.core.publisher.Mono

interface NotificationService {
    fun kafkaReceivingCredit(): Mono<Void>
    fun kafkaReceivingCreditCheck(): Mono<Void>
    fun kafkaReceivingCreditPayed(): Mono<Void>
    fun kafkaReceivingCreditPayment(): Mono<Void>
    fun kafkaReceivingCreditCollector(): Mono<Void>

}