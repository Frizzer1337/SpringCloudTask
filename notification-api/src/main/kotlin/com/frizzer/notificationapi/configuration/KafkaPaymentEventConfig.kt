package com.frizzer.notificationapi.configuration

import com.frizzer.contractapi.entity.payment.PaymentEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
open class KafkaPaymentEventConfig {
    @Bean
    open fun kafkaCreditPaymentConsumerFactoryTemplate(
        @Value(value = "\${kafka.bootstrapAddress}") bootstrapAddress: String? = null,
        @Value(value = "\${group.id}") groupId: String? = null,
        @Value(value = "\${topic.approve}") topic: String? = null
    ): KafkaReceiver<String, PaymentEvent> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        var receiverOptions: ReceiverOptions<String, PaymentEvent> = ReceiverOptions.create(props)
        receiverOptions =
            receiverOptions.withValueDeserializer(JsonDeserializer(PaymentEvent::class.java))
        receiverOptions = receiverOptions.subscription(setOf(topic))
        return KafkaReceiver.create(receiverOptions)
    }

}