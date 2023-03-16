package com.frizzer.notificationapi.configuration

import com.frizzer.contractapi.entity.credit.CreditCheckEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.ReceiverOptions
import java.util.*

@Configuration
open class KafkaCreditCheckReceiverConfig {
    @Bean
    open fun kafkaCreditCheckConsumerFactoryTemplate(
        @Value(value = "\${kafka.bootstrap}") bootstrapAddress: String? = null,
        @Value(value = "\${kafka.group.id}") groupId: String? = null,
        @Value(value = "\${kafka.topic.check}") topic: String? = null
    ): ReceiverOptions<String, CreditCheckEvent> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        var receiverOptions: ReceiverOptions<String, CreditCheckEvent> =
            ReceiverOptions.create(props)
        receiverOptions =
            receiverOptions.withValueDeserializer(JsonDeserializer(CreditCheckEvent::class.java))
        receiverOptions = receiverOptions.subscription(setOf(topic))
        return receiverOptions.subscription(Collections.singletonList(topic))
    }

    @Bean
    open fun reactiveKafkaCreditCheckConsumerTemplate(kafkaReceiverOptions: ReceiverOptions<String, CreditCheckEvent>): ReactiveKafkaConsumerTemplate<String, CreditCheckEvent> {
        return ReactiveKafkaConsumerTemplate(kafkaReceiverOptions)
    }

}