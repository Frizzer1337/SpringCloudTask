package com.frizzer.approveapi.service

import com.frizzer.approveapi.repository.BorrowerRepository
import com.frizzer.contractapi.entity.Borrower
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BorrowerService(private val borrowerRepository: BorrowerRepository) {
    fun findBorrowerById(borrowerId: String): Mono<Borrower> {
        return borrowerRepository.findBorrowerById(borrowerId)
    }
}
