package com.frizzer.approveapi.repository

import com.frizzer.contractapi.entity.credit.Credit
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : ReactiveMongoRepository<Credit, String>