package com.frizzer.kafka.paymentmicroservice.service

import com.frizzer.kafka.paymentmicroservice.repository.CollectorRepository
import com.frizzer.kafkaapi.entity.CollectorCredit
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CollectorService(private val collectorRepository: CollectorRepository) {
    fun save(collectorCredit: CollectorCredit): Mono<CollectorCredit> {
        return collectorRepository.save(collectorCredit)
    }

}