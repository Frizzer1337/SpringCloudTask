package com.frizzer.borrowermicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import reactor.core.publisher.Mono

interface CreditRepository {

    fun save(credit: Credit?): Mono<Boolean>
}