package com.frizzer.approvemicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.CreditStatus
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import com.mongodb.reactivestreams.client.MongoCollection
import reactor.core.publisher.Mono

class MongoCreditRepository(private val collection: MongoCollection<Credit>) : CreditRepository{

    override fun changeStatus(credit: Credit, creditStatus: CreditStatus): Mono<Credit> {
        return Mono.from(
            collection.findOneAndUpdate(
                Filters.eq("_id", credit.id),
                Updates.set("creditStatus", creditStatus),
                FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            )
        )
    }

}