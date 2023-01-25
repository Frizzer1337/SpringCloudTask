package com.frizzer.approvemicroservice.service

import com.frizzer.kafkaapi.entity.Borrower
import reactor.core.publisher.Mono

interface BorrowerService {

    fun findBorrowerById(borrowerId: String?): Mono<Borrower>

}