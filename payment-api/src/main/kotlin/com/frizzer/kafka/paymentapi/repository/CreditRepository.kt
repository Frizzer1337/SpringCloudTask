package com.frizzer.kafka.paymentapi.repository

import com.frizzer.contractapi.entity.Credit
import com.frizzer.contractapi.entity.CreditStatus
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CreditRepository : ReactiveMongoRepository<Credit, String> {

    fun findCreditById(id: String): Mono<Credit>
    fun findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(
        lastPaymentDate: String,
        creditStatus: CreditStatus
    ): Flux<Credit>
    fun findCreditsByCreditStatus(creditStatus: CreditStatus): Flux<Credit>
    fun findCreditsByCreditBalanceEqualsAndCreditStatusEquals(
        creditBalance: Int,
        creditStatus: CreditStatus
    ): Flux<Credit>
}
