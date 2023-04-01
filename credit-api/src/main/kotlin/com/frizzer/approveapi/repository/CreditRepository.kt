package com.frizzer.approveapi.repository

import com.frizzer.contractapi.entity.credit.Credit
import com.frizzer.contractapi.entity.credit.CreditStatus
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface CreditRepository : R2dbcRepository<Credit, String> {
    fun findCreditsByLastPaymentDateIsLessThanAndCreditStatusEquals(
        lastPaymentDate: String,
        creditStatus: CreditStatus
    ): Flux<Credit>

    fun findCreditsByCreditBalanceEqualsAndCreditStatusEquals(
        creditBalance: Int,
        creditStatus: CreditStatus
    ): Flux<Credit>

}