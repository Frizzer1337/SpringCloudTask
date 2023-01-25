package com.frizzer.approvemicroservice.repository

import com.frizzer.kafkaapi.entity.Borrower
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoCollection
import reactor.core.publisher.Mono

class MongoBorrowerRepository(private val collection: MongoCollection<Borrower>) : BorrowerRepository {
    override fun findBorrowerById(borrowerId: String?): Mono<Borrower> {
        return Mono.from(collection.find(Filters.eq("_id", borrowerId)).first())
    }

}