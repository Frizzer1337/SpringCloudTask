package com.frizzer.borrowermicroservice.service.impl

import com.frizzer.borrowermicroservice.repository.CreditRepository
import com.frizzer.borrowermicroservice.service.CreditService
import com.frizzer.kafkaapi.entity.Credit
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult


@Service
class CreditServiceImpl(
    val creditRepository: CreditRepository,
    val kafkaTemplate: ReactiveKafkaProducerTemplate<String, Credit>
) : CreditService {
    private val log = LoggerFactory.getLogger(CreditServiceImpl::class.java)

    override fun takeCredit(credit: Credit): Mono<Boolean> {
        return creditRepository
            .save(credit)
            .flatMap { x ->
                kafkaTemplate
                    .send("CREDIT_APPROVE", credit)
                    .doOnSuccess { result: SenderResult<Void> ->
                        log.info(
                            "sent {} offset: {}", credit, result.recordMetadata().offset()
                        )
                    }
                    .thenReturn(x)
            }
    }


}
