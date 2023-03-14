package com.frizzer.approveapi.configuration.kafka

import com.frizzer.contractapi.entity.credit.Credit
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.ReceiverOptions

@Configuration
open class KafkaConsumerConfig {
    @Bean
    open fun kafkaConsumerFactoryTemplate(
        @Value(value = "\${kafka.bootstrapAddress}") bootstrapAddress: String? = null,
        @Value(value = "\${group.approve.id}") groupId: String? = null,
        @Value(value = "\${topic.approve}") topic: String? = null
    ): ReactiveKafkaConsumerTemplate<String, Credit>? {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        var receiverOptions: ReceiverOptions<String, Credit> = ReceiverOptions.create(props)
        val deserializer = JsonDeserializer(Credit::class.java, false)
        receiverOptions = receiverOptions.withValueDeserializer(deserializer)
        receiverOptions = receiverOptions.subscription(setOf(topic))
        return ReactiveKafkaConsumerTemplate(receiverOptions)
    }

}