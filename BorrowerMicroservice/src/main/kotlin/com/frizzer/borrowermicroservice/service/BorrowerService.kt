package com.frizzer.borrowermicroservice.service

import com.frizzer.borrowermicroservice.repository.BorrowerRepository
import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BorrowerService(private val borrowerRepository: BorrowerRepository) {
    fun register(borrower: Borrower): Mono<Borrower> {
        return borrowerRepository.save(borrower)
    }
}