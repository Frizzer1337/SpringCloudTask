package com.frizzer.borrowermicroservice.configuration

import com.frizzer.borrowermicroservice.repository.BorrowerRepository
import com.frizzer.borrowermicroservice.repository.MongoBorrowerRepository
import com.frizzer.kafkaapi.entity.Borrower
import org.springframework.beans.factory.annotation.Autowired
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