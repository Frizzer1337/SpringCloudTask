package com.frizzer.clientapi.configuration

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.ReactiveMongoTransactionManager
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration


@Configuration
open class MongoReactiveConfiguration : AbstractReactiveMongoConfiguration() {

    @Value(value = "\${spring.data.mongodb.database}")
    private var databaseName: String = ""

    @Bean
    open fun transactionManager(dbFactory: ReactiveMongoDatabaseFactory): ReactiveMongoTransactionManager {
        return ReactiveMongoTransactionManager(dbFactory)
    }

    override fun getDatabaseName(): String {
        return databaseName
    }

    override fun reactiveMongoClient(): MongoClient {
        return MongoClients.create()
    }

}
