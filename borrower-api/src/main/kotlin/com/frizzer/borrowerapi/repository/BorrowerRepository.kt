package com.frizzer.borrowerapi.repository

import com.frizzer.contractapi.entity.borrower.Borrower
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface BorrowerRepository : ReactiveMongoRepository<Borrower, String> {
    fun save(borrower: Borrower): Mono<Borrower>
}