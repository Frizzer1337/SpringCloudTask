package com.frizzer.notificationmicroservice.service.impl

import com.frizzer.kafkaapi.entity.*
import com.frizzer.notificationmicroservice.service.NotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver

@Service
class NotificationServiceImpl(
    private val kafkaReceiver: KafkaReceiver<String, Credit>,
    private val kafkaCreditCheckReceiver: KafkaReceiver<String, CreditCheckEvent>,
    private val kafkaCreditPayedReceiver: KafkaReceiver<String, CreditPayedEvent>,
    private val kafkaCreditCollectorReceiver: KafkaReceiver<String, CollectorEvent>,
    private val kafkaCreditPaymentReceiver: KafkaReceiver<String, PaymentEvent>

) : NotificationService {

    var log: Logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCredit(): Mono<Void> {
        return kafkaReceiver
            .receive()
            .doOnNext { x ->
                log.info(
                    "Credit {} was send to approve offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditCheck(): Mono<Void> {
        return kafkaCreditCheckReceiver
            .receive()
            .doOnNext { x ->
                log.info(
                    "Credit {} was checked at offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditPayed(): Mono<Void> {
        return kafkaCreditPayedReceiver
            .receive()
            .doOnNext { x ->
                log.info(
                    "Credit {} was payed at offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditPayment(): Mono<Void> {
        return kafkaCreditPaymentReceiver
            .receive()
            .doOnNext { x ->
                log.info(
                    "Credit {} had payment at offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditCollector(): Mono<Void> {
        return kafkaCreditCollectorReceiver
            .receive()
            .doOnNext { x ->
                log.info(
                    "Credit {} was sent to collectors at offset: {}",
                    x.value().javaClass,
                    x.offset()
                )
            }.then()
    }


}