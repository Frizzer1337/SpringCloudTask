package com.frizzer.approveapi.configuration.kafka

import com.frizzer.contractapi.entity.credit.CreditCheckEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.support.serializer.JsonSerializer
import reactor.kafka.sender.SenderOptions

@Configuration
open class KafkaCreditCheckProducerConfig {
    @Bean
    open fun reactiveKafkaTemplate(@Value(value = "\${kafka.bootstrap}") bootstrapAddress: String? = null): ReactiveKafkaProducerTemplate<String, CreditCheckEvent> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return ReactiveKafkaProducerTemplate<String, CreditCheckEvent>(SenderOptions.create(props))
    }
}