package com.frizzer.kafka.paymentmicroservice.controller

import com.frizzer.kafka.paymentmicroservice.service.impl.CreditServiceImpl
import com.frizzer.kafkaapi.entity.Payment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/payment")
class PaymentController(val creditService: CreditServiceImpl) {

    @PostMapping("/pay")
    fun register(@RequestBody payment: Payment): ResponseEntity<Mono<Boolean>> {
        return ResponseEntity.ok(creditService.payAndSavePayment(payment))
    }
}