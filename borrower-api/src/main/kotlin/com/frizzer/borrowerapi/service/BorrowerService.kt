package com.frizzer.borrowerapi.service

import com.frizzer.borrowerapi.repository.BorrowerRepository
import com.frizzer.contractapi.entity.borrower.Borrower
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BorrowerService(private val borrowerRepository: BorrowerRepository) {
    fun register(borrower: Borrower): Mono<Borrower> {
        return borrowerRepository.save(borrower)
    }
}