package com.frizzer.kafka.paymentmicroservice.configuration

import com.frizzer.kafka.paymentmicroservice.repository.CreditRepository
import com.frizzer.kafka.paymentmicroservice.repository.MongoCreditRepository
import com.frizzer.kafkaapi.entity.Credit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CreditRepositoryConfiguration(private val borrowerMongoConfiguration: BorrowerMongoConfiguration) {
    @Bean
    open fun creditRepository(): CreditRepository {
        return MongoCreditRepository(
            borrowerMongoConfiguration.database().getCollection("credit", Credit::class.java)
        )
    }
}