package com.frizzer.borrowermicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import com.mongodb.reactivestreams.client.MongoCollection
import reactor.core.publisher.Mono

class MongoCreditRepository(private val collection: MongoCollection<Credit>) : CreditRepository {

    override fun save(credit: Credit?): Mono<Boolean> {
        return Mono.from(collection.insertOne(credit)).map{true}.defaultIfEmpty(false)
    }

}