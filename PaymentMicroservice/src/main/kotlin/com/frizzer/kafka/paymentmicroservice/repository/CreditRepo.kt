package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.CreditStatus
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CreditRepo : ReactiveMongoRepository<Credit, String> {

    fun findCreditById(id: String): Mono<Credit>

    fun findCreditsByLastPaymentDateIsLessThan(lastPaymentDate: String): Flux<Credit>

    fun findCreditsByCreditStatus(creditStatus: CreditStatus): Flux<Credit>
}
