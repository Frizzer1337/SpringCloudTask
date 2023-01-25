package com.frizzer.borrowermicroservice.repository

import com.frizzer.kafkaapi.entity.Borrower
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoCollection
import reactor.core.publisher.Mono

class MongoBorrowerRepository(private val collection: MongoCollection<Borrower>) : BorrowerRepository {

    override fun save(borrower: Borrower?): Mono<Boolean> {
        return Mono.from(collection.insertOne(borrower)).map{true}.defaultIfEmpty(false)
    }

}