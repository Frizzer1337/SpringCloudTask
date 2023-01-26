package com.frizzer.kafka.paymentmicroservice.service.impl

import com.frizzer.kafka.paymentmicroservice.repository.CreditRepository
import com.frizzer.kafka.paymentmicroservice.service.CreditService
import com.frizzer.kafkaapi.entity.*
import kafka.practice.api.entity.CollectorCredit
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.Disposable
import reactor.core.publisher.Mono

@Service
class CreditServiceImpl(
    val creditRepository: CreditRepository,
    val paymentService: PaymentServiceImpl,
    val collectorService: CollectorServiceImpl,
    val kafkaTemplate: ReactiveKafkaProducerTemplate<String, PaymentEvent>,
    val kafkaCollectorTemplate: ReactiveKafkaProducerTemplate<String, CollectorEvent>,
    val kafkaCreditPayedTemplate: ReactiveKafkaProducerTemplate<String, CreditPayedEvent>
) : CreditService {
    private val log: Logger = LoggerFactory.getLogger(CreditServiceImpl::class.java)

    override fun pay(payment: Payment): Mono<Boolean> {
        return creditRepository
            .pay(payment)
            .map{ x ->
                creditRepository
                    .checkIfCreditPayed(x)
                    .map{ creditPayed ->
                        if (java.lang.Boolean.TRUE == creditPayed) {
                            kafkaCreditPayedTemplate
                                .send("CREDIT_PAYED", CreditPayedEvent(x))
                                .doOnSuccess {result ->
                                    log.info(
                                        "Credit payed event sent {} offset: {}",
                                        CreditPayedEvent(x),
                                        result.recordMetadata().offset()
                                    )
                                }.map{true}
                        } else {
                            kafkaTemplate
                                .send("CREDIT_PAYMENT", PaymentEvent(x))
                                .doOnSuccess { result ->
                                    log.info(
                                        "Credit payment event sent {} offset: {}",
                                        PaymentEvent(x),
                                        result.recordMetadata().offset()
                                    )
                                }.map{true}
                        }
                    }
            }
            .map { true }
            .defaultIfEmpty(false)
    }

    @Transactional
    override fun payAndSavePayment(payment: Payment): Mono<Boolean> {
        return paymentService.save(payment).then(pay(payment))
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendPenalty(): Disposable {
        log.info("Credit's were checked")
        return creditRepository.sendPenalty().subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun changeStatusForBigPenalty(): Disposable {
        return creditRepository.checkCreditToSendCollector().subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendToCollectorsForBigPenalty(): Disposable {
        return creditRepository
            .findCreditToSendCollector()
            .flatMap { x ->
                collectorService
                    .save(CollectorCredit(x))
                    .then(
                        kafkaCollectorTemplate
                            .send("CREDIT_TO_COLLECTOR", CollectorEvent(x.id,x.creditStatus))
                            .doOnNext { result ->
                                log.info(
                                    "sent {} offset: {}",
                                    x,
                                    result.recordMetadata().offset()
                                )
                            }
                    )
            }
            .then(creditRepository.markCreditSendToCollector())
            .subscribe()
    }

    @Scheduled(fixedDelay = 10000)
    override fun sendWarnForBigPenalty(): Disposable {
        return creditRepository
            .checkCreditToWarn()
            .doOnNext { x -> log.warn("Credit have big penalty {}", x) }
            .subscribe()
    }
}