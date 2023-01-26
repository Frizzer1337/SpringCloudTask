package com.frizzer.kafka.paymentmicroservice.service.impl

import com.frizzer.kafka.paymentmicroservice.repository.CollectorRepository
import com.frizzer.kafka.paymentmicroservice.service.CollectorService
import kafka.practice.api.entity.CollectorCredit
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CollectorServiceImpl(private val collectorRepository : CollectorRepository) : CollectorService {

    override fun save(collectorCredit: CollectorCredit): Mono<Boolean> {
        return collectorRepository.save(collectorCredit)
    }

}