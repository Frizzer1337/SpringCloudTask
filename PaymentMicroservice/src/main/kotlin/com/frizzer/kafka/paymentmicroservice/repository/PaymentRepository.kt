package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.Payment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PaymentRepository : ReactiveMongoRepository<Payment, String>