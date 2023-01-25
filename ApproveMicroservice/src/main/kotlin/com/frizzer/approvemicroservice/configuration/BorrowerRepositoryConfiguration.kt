package com.frizzer.approvemicroservice.configuration

import com.frizzer.approvemicroservice.repository.BorrowerRepository
import com.frizzer.approvemicroservice.repository.MongoBorrowerRepository
import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BorrowerRepositoryConfiguration @Autowired constructor(private val borrowerMongoConfiguration: BorrowerMongoConfiguration) {
    @Bean
    fun borrowerRepository(): BorrowerRepository {
        return MongoBorrowerRepository(
            borrowerMongoConfiguration.database().getCollection("borrower", Borrower::class.java)
        )
    }
}