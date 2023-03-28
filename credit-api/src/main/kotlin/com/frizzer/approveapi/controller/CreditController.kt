package com.frizzer.approveapi.controller

import com.frizzer.approveapi.service.CreditService
import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.contractapi.entity.payment.PaymentDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/credit")
class CreditController(
    private val creditService: CreditService
) {

    @PostMapping("/")
    fun takeCredit(@RequestBody creditDto: CreditDto): ResponseEntity<Mono<CreditDto>> {
        return ResponseEntity.ok(creditService.save(creditDto))
    }

    @PostMapping("/pay")
    fun pay(@RequestBody paymentDto: PaymentDto): ResponseEntity<Mono<CreditDto>> {
        return ResponseEntity.ok(creditService.pay(paymentDto))
    }

}