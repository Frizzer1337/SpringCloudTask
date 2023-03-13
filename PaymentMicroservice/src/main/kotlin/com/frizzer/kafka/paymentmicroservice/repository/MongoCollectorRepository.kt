package com.frizzer.kafka.paymentmicroservice.repository

import com.mongodb.reactivestreams.client.MongoCollection
import kafka.practice.api.entity.CollectorCredit
import reactor.core.publisher.Mono

class MongoCollectorRepository(private val collectorCollection: MongoCollection<CollectorCredit>) :
    CollectorRepository {
    override fun save(collectorCredit: CollectorCredit): Mono<Boolean> {
        return Mono.from(collectorCollection.insertOne(collectorCredit))
            .map { true }
            .defaultIfEmpty(false)
    }
}