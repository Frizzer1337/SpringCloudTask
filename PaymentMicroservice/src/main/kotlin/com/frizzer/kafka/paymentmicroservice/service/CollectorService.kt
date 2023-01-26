package com.frizzer.kafka.paymentmicroservice.service

import kafka.practice.api.entity.CollectorCredit
import reactor.core.publisher.Mono

interface CollectorService {

    fun save(collectorCredit: CollectorCredit): Mono<Boolean>

}