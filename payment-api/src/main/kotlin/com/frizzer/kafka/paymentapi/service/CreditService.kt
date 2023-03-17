package com.frizzer.kafka.paymentapi.service

import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.contractapi.entity.payment.PaymentDto
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(value = "credit-api", url = "http://localhost:8080/credit")
@Service
interface CreditService {
    @RequestMapping(method = [RequestMethod.POST], value = ["/pay"])
    fun pay(payment: PaymentDto): Mono<CreditDto>

}