package com.frizzer.approveapi.repository

import com.frizzer.contractapi.entity.credit.Credit
import com.frizzer.contractapi.entity.credit.CreditStatus
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CreditRepository : ReactiveMongoRepository<Credit, String> {
    fun findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(
        lastPaymentDate: String,
        creditStatus: CreditStatus
    ): Flux<Credit>

    fun findCreditsByCreditBalanceEqualsAndCreditStatusEquals(
        creditBalance: Int,
        creditStatus: CreditStatus
    ): Flux<Credit>

    fun findCreditById(id: String): Mono<Credit>
}