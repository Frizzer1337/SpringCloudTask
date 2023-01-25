package com.frizzer.borrowermicroservice.repository

import com.frizzer.kafkaapi.entity.Borrower
import reactor.core.publisher.Mono

interface BorrowerRepository {

    fun save(borrower : Borrower?): Mono<Boolean>
}