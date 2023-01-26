package com.frizzer.notificationmicroservice.configuration

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.CreditPayedEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaCreditPayedConfig {

    private lateinit var receiverOptions: ReceiverOptions<String, CreditPayedEvent>

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String? = null

    @Value(value = "\${group.id}")
    private val groupId: String? = null

    @Value(value = "\${topic.payed}")
    private val topic: String? = null

    @Bean
    fun kafkaCreditPayedConsumerFactoryTemplate(): KafkaReceiver<String, CreditPayedEvent> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        receiverOptions = ReceiverOptions.create(props)
        receiverOptions = receiverOptions.withValueDeserializer(JsonDeserializer(CreditPayedEvent::class.java))
        receiverOptions = receiverOptions.subscription(setOf(topic))
        return KafkaReceiver.create(receiverOptions)
    }

}