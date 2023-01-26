package com.frizzer.kafka.paymentmicroservice.repository

import kafka.practice.api.entity.CollectorCredit
import reactor.core.publisher.Mono

interface CollectorRepository {
    fun save(collectorCredit: CollectorCredit): Mono<Boolean>
}