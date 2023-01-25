package com.frizzer.borrowermicroservice.service

import com.frizzer.kafkaapi.entity.Credit
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
interface CreditService {
    fun takeCredit(credit: Credit): Mono<Boolean>
}
