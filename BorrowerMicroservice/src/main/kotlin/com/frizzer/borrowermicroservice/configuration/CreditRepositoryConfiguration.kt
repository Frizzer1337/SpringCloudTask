package com.frizzer.borrowermicroservice.configuration

import com.frizzer.borrowermicroservice.repository.CreditRepository
import com.frizzer.borrowermicroservice.repository.MongoCreditRepository
import com.frizzer.kafkaapi.entity.Credit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreditRepositoryConfiguration(private val borrowerMongoConfiguration: BorrowerMongoConfiguration) {
    @Bean
    fun creditRepository(): CreditRepository {
        return MongoCreditRepository(
            borrowerMongoConfiguration.database().getCollection("credit", Credit::class.java)
        )
    }
}