package com.frizzer.kafka.paymentmicroservice.configuration

import com.frizzer.kafka.paymentmicroservice.repository.CollectorRepository
import com.frizzer.kafka.paymentmicroservice.repository.MongoCollectorRepository
import kafka.practice.api.entity.CollectorCredit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CollectorRepositoryConfiguration (private val borrowerMongoConfiguration: BorrowerMongoConfiguration) {
    @Bean
    fun collectorRepository(): CollectorRepository {
        return MongoCollectorRepository(
            borrowerMongoConfiguration.database().getCollection("collectorCredit", CollectorCredit::class.java)
        )
    }
}