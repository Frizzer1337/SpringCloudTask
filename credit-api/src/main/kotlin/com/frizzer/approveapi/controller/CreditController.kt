package com.frizzer.approveapi.controller

import com.frizzer.approveapi.service.CreditService
import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.contractapi.entity.exception.PaymentApproveException
import com.frizzer.contractapi.entity.payment.PaymentDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/credit")
class CreditController(
    private val creditService: CreditService
) {

    @PostMapping("/create")
    fun takeCredit(@RequestBody creditDto: CreditDto): ResponseEntity<Mono<CreditDto>> {
        return ResponseEntity.ok(creditService.save(creditDto))
    }

    @PostMapping("/pay")
    fun pay(@RequestBody paymentDto: PaymentDto): ResponseEntity<Mono<CreditDto>> {
        return ResponseEntity.ok(creditService.pay(paymentDto))
    }

    @ExceptionHandler(PaymentApproveException::class)
    fun handlePaymentApproveException(exception: PaymentApproveException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: java.lang.Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.message)
    }

}