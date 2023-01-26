package com.frizzer.kafka.paymentmicroservice.configuration

import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BorrowerMongoConfiguration {
    @Bean
    fun database(): MongoDatabase {
        val mongo = MongoClients.create()
        val pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        )
        return mongo.getDatabase("creditProject").withCodecRegistry(pojoCodecRegistry)
    }
}