package com.frizzer.approveapi.repository

import com.frizzer.contractapi.entity.Borrower
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface BorrowerRepository : ReactiveMongoRepository<Borrower, String> {
    fun findBorrowerById(borrowerId: String?): Mono<Borrower>
}