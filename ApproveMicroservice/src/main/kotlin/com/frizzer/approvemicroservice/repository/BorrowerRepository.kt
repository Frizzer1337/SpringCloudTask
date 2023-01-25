package com.frizzer.approvemicroservice.repository

import com.frizzer.kafkaapi.entity.Borrower
import reactor.core.publisher.Mono

interface BorrowerRepository {

    fun findBorrowerById(borrowerId: String?): Mono<Borrower>

}