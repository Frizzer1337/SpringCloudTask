package com.frizzer.kafka.paymentapi.controller

import com.frizzer.contractapi.entity.payment.PaymentDto
import com.frizzer.kafka.paymentapi.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/payment")
class PaymentController(private val paymentService: PaymentService) {

    @PostMapping("/")
    fun pay(@RequestBody paymentDto: PaymentDto): ResponseEntity<Mono<PaymentDto>> {
        return ResponseEntity.ok(paymentService.pay(paymentDto))
    }
}