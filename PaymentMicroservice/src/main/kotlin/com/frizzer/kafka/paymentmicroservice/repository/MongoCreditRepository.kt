package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.Payment
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.Document
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class MongoCreditRepository(private val collection: MongoCollection<Credit>) : CreditRepository {

    override fun pay(payment: Payment): Mono<Credit> {
        return Mono.from(
            collection.findOneAndUpdate(
                Filters.eq("_id", payment.creditId),
                Updates.combine(
                    Updates.inc("creditBalance", -1 * payment.payment),
                    Updates.inc("penalty", -1 * payment.payment),
                    Updates.set("lastPaymentDate", LocalDateTime.now().toString())
                ),
                FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            )
        )
    }

    override fun checkIfCreditPayed(credit: Credit): Mono<Boolean> {
        return Mono.from(
            collection.findOneAndUpdate(
                Filters.lte("creditBalance", 0), Updates.set("creditStatus", "CREDIT_PAYED")
            )
        )
            .map { true }
            .defaultIfEmpty(false)
    }

    override fun sendPenalty(): Mono<Boolean> {
        return Mono.from(
            collection.updateMany(
                Filters.lt("lastPaymentDate", LocalDateTime.now().minusDays(1).toString()),
                Updates.combine(Updates.inc("penalty", 50), Updates.inc("creditBalance", 50))
            )
        )
            .map { true }
            .defaultIfEmpty(false)
    }

    override fun checkCreditToSendCollector(): Flux<UpdateResult> {
        return Flux.from(
            collection.updateMany(
                Document(
                    "\$and",
                    listOf(
                        Document(
                            "\$expr",
                            Document(
                                "\$gt",
                                listOf(
                                    Document(
                                        "\$divide", listOf("\$penalty", "\$creditBalance")
                                    ),
                                    0.5
                                )
                            )
                        ),
                        Document(
                            "\$expr", Document("\$eq", listOf("\$creditStatus", "APPROVED"))
                        )
                    )
                ),
                listOf(Document("\$set", Document("creditStatus", "NEED_COLLECTOR")))
            )
        )
    }

    override fun findCreditToSendCollector(): Flux<Credit> {
        return Flux.from(collection.find(Filters.eq("creditStatus", "NEED_COLLECTOR")))
    }

    override fun markCreditSendToCollector(): Mono<Boolean> {
        return Mono.from(
            collection.updateMany(
                Filters.eq("creditStatus", "NEED_COLLECTOR"),
                Updates.set("creditStatus", "SEND_TO_COLLECTOR")
            )
        )
            .map { true }
            .defaultIfEmpty(false)
    }

    override fun checkCreditToWarn(): Flux<Credit> {
        return Flux.from(
            collection.aggregate(
                listOf(
                    Document(
                        "\$match",
                        Document(
                            "\$expr",
                            Document(
                                "\$gt",
                                listOf(
                                    Document(
                                        "\$divide", listOf("\$penalty", "\$creditBalance")
                                    ),
                                    0.3
                                )
                            )
                        )
                    ),
                    Document(
                        "\$match",
                        Document(
                            "\$expr",
                            Document(
                                "\$lt",
                                listOf(
                                    Document(
                                        "\$divide", listOf("\$penalty", "\$creditBalance")
                                    ),
                                    0.5
                                )
                            )
                        )
                    )
                )
            )
        )
    }

}