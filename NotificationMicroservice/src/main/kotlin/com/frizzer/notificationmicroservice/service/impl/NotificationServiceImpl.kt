package com.frizzer.notificationmicroservice.service.impl

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.CreditCheckEvent
import com.frizzer.notificationmicroservice.service.NotificationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverRecord

@Service
class NotificationServiceImpl(
    private val kafkaReceiver: KafkaReceiver<String, Credit>,
    private val kafkaCreditCheckReceiver : KafkaReceiver<String,CreditCheckEvent>
) : NotificationService {

    var log: Logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCredit(): Mono<Void> {
        return kafkaReceiver
            .receive()
            .doOnNext { x: ReceiverRecord<String, Credit> ->
                log.info(
                    "Credit {} was send to approve offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceivingCreditCheck(): Mono<Void> {
        return kafkaCreditCheckReceiver
            .receive()
            .doOnNext { x: ReceiverRecord<String, CreditCheckEvent> ->
                log.info(
                    "Credit {} was checked at offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }


}