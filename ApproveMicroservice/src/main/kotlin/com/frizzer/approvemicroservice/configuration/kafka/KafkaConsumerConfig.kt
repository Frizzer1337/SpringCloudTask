package com.frizzer.approvemicroservice.configuration.kafka

import com.frizzer.kafkaapi.entity.Credit
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaConsumerConfig {

    private lateinit var receiverOptions: ReceiverOptions<String, Credit>

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Value(value = "\${group.approve.id}")
    private val groupId: String? = null

    @Value(value = "\${topic.approve}")
    private val topic: String? = null

    @Bean
    fun kafkaConsumerFactoryTemplate(): ReactiveKafkaConsumerTemplate<String, Credit>? {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        receiverOptions = ReceiverOptions.create(props)
        val deserializer = JsonDeserializer(Credit::class.java, false)
        deserializer.addTrustedPackages("kafka.practice.*")
        receiverOptions = receiverOptions.withValueDeserializer(deserializer)
        receiverOptions = receiverOptions.subscription(setOf(topic))
        return ReactiveKafkaConsumerTemplate(receiverOptions)
    }

}