package com.frizzer.clientapi.configuration.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
open class KafkaTopicConfig {
    @Bean
    open fun kafkaAdmin(@Value(value = "\${kafka.bootstrap}") bootstrapAddress: String? = null): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }
}
