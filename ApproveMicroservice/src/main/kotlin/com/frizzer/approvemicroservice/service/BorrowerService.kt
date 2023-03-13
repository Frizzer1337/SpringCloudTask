package com.frizzer.approvemicroservice.service

import com.frizzer.approvemicroservice.repository.BorrowerRepository
import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BorrowerService(private val borrowerRepository: BorrowerRepository) {
    fun findBorrowerById(borrowerId: String): Mono<Borrower> {
        return borrowerRepository.findBorrowerById(borrowerId)
    }
}
