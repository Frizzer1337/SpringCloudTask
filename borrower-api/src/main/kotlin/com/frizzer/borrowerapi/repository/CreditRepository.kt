package com.frizzer.borrowerapi.repository

import com.frizzer.contractapi.entity.Credit
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CreditRepository : ReactiveMongoRepository<Credit, String> {
    fun save(credit: Credit): Mono<Credit>

}
