package com.frizzer.approvemicroservice.repository

import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.CreditStatus
import reactor.core.publisher.Mono

interface CreditRepository{

    fun changeStatus(credit: Credit, creditStatus: CreditStatus): Mono<Credit>

}