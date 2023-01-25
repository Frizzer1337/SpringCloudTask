package com.frizzer.approvemicroservice.service.impl

import com.frizzer.approvemicroservice.repository.BorrowerRepository
import com.frizzer.approvemicroservice.service.BorrowerService
import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BorrowerServiceImpl(val borrowerRepository: BorrowerRepository) : BorrowerService {

    override fun findBorrowerById(borrowerId: String?): Mono<Borrower> {
        return borrowerRepository.findBorrowerById(borrowerId)
    }
}
