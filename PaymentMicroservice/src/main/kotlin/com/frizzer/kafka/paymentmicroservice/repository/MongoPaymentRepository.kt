package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.Payment
import com.mongodb.reactivestreams.client.MongoCollection
import reactor.core.publisher.Mono

class MongoPaymentRepository(private val paymentCollection : MongoCollection<Payment>) : PaymentRepository {

    override fun save(payment: Payment): Mono<Boolean> {
        return Mono.from(paymentCollection.insertOne(payment))
            .map {true}
            .defaultIfEmpty(false)
    }

}