package com.frizzer.kafka.paymentmicroservice.repository

import com.frizzer.kafkaapi.entity.CollectorCredit
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CollectorRepository : ReactiveMongoRepository<CollectorCredit, String>