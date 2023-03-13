package com.frizzer.borrowermicroservice.repository

import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface BorrowerRepository : ReactiveMongoRepository<Borrower, String> {
    fun save(borrower: Borrower): Mono<Borrower>
}