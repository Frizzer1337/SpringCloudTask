package com.frizzer.borrowermicroservice.service

import com.frizzer.borrowermicroservice.repository.CreditRepository
import com.frizzer.kafkaapi.entity.Credit
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, Credit>
) {
    private val log = LoggerFactory.getLogger(CreditService::class.java)
    fun takeCredit(credit: Credit): Mono<Credit> {
        return creditRepository
            .save(credit)
            .flatMap {
                kafkaTemplate
                    .send("CREDIT_APPROVE", it)
                    .doOnSuccess { result ->
                        log.info("sent {} offset: {}", credit, result.recordMetadata().offset())
                    }
                    .thenReturn(it)
            }
    }


}
