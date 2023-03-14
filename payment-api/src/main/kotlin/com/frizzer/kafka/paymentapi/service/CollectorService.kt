package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.collector.CollectorCredit
import com.frizzer.kafka.paymentapi.repository.CollectorRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CollectorService(private val collectorRepository: CollectorRepository) {
    fun save(collectorCredit: CollectorCredit): Mono<CollectorCredit> {
        return collectorRepository.save(collectorCredit)
    }

}