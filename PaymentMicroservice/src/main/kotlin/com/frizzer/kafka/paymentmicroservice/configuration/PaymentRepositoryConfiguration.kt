package com.frizzer.kafka.paymentmicroservice.configuration

import com.frizzer.kafka.paymentmicroservice.repository.MongoPaymentRepository
import com.frizzer.kafka.paymentmicroservice.repository.PaymentRepository
import com.frizzer.kafkaapi.entity.Payment
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class PaymentRepositoryConfiguration(private val borrowerMongoConfiguration: BorrowerMongoConfiguration) {
    @Bean
    open fun paymentRepository(): PaymentRepository {
        return MongoPaymentRepository(
            borrowerMongoConfiguration.database().getCollection("payment", Payment::class.java)
        )
    }
}