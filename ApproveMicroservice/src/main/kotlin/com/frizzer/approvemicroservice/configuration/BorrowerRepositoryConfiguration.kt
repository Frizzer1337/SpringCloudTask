package com.frizzer.approvemicroservice.configuration

import com.frizzer.approvemicroservice.repository.BorrowerRepository
import com.frizzer.approvemicroservice.repository.MongoBorrowerRepository
import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BorrowerRepositoryConfiguration(private val borrowerMongoConfiguration: BorrowerMongoConfiguration) {
    @Bean
    fun borrowerRepository(): BorrowerRepository {
        return MongoBorrowerRepository(
            borrowerMongoConfiguration.database().getCollection("borrower", Borrower::class.java)
        )
    }
}