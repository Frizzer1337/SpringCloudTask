package com.frizzer.kafka.paymentapi.repository

import com.frizzer.contractapi.entity.Payment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PaymentRepository : ReactiveMongoRepository<Payment, String>