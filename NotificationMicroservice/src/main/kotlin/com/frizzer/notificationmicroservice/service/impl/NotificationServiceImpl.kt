package com.frizzer.notificationmicroservice.service.impl

import com.frizzer.kafkaapi.entity.Credit
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
class NotificationServiceImpl(private val kafkaReceiver: KafkaReceiver<String, Credit>) : NotificationService {

    var log: Logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceiving(): Mono<Void> {
        return kafkaReceiver
            .receive()
            .doOnNext { x: ReceiverRecord<String, Credit> ->
                log.info(
                    "Credit {} was send to approve offset: {}", x.value().javaClass, x.offset()
                )
            }.then()
    }


}