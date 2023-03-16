package com.frizzer.notificationapi.service

import com.frizzer.contractapi.entity.credit.CreditCheckEvent
import com.frizzer.contractapi.entity.credit.CreditPayedEvent
import com.frizzer.contractapi.entity.payment.PaymentEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class NotificationService(
    private val kafkaCreditCheckReceiver: ReactiveKafkaConsumerTemplate<String, CreditCheckEvent>,
    private val kafkaCreditPayedReceiver: ReactiveKafkaConsumerTemplate<String, CreditPayedEvent>,
    private val kafkaPaymentReceiver: ReactiveKafkaConsumerTemplate<String, PaymentEvent>

) {

    companion object {
        var log: Logger = LoggerFactory.getLogger(NotificationService::class.java)
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditCheck(): Flux<ConsumerRecord<String, CreditCheckEvent>> {
        return kafkaCreditCheckReceiver
            .receiveAutoAck()
            .doOnNext { x ->
                log.info(
                    "Credit {} was checked at offset: {}", x.value().javaClass, x.offset()
                )
            }
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditPayed(): Flux<ConsumerRecord<String, CreditPayedEvent>> {
        return kafkaCreditPayedReceiver
            .receiveAutoAck()
            .doOnNext { x ->
                log.info(
                    "Credit {} was payed at offset: {}", x.value().javaClass, x.offset()
                )
            }
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingPayment(): Flux<ConsumerRecord<String, PaymentEvent>> {
        return kafkaPaymentReceiver
            .receiveAutoAck()
            .doOnNext { x ->
                log.info(
                    "Credit {} had payment at offset: {}", x.value().javaClass, x.offset()
                )
            }
    }


}