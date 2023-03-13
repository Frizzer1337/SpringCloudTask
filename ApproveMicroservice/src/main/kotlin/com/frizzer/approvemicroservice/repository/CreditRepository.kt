package com.frizzer.approvemicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : ReactiveMongoRepository<Credit, String>