package com.frizzer.borrowermicroservice.service

import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
interface BorrowerService {
    fun register(borrower: Borrower): Mono<Borrower>
}