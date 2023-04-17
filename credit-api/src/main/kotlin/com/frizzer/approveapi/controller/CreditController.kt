package com.frizzer.approveapi.controller

import com.frizzer.approveapi.service.CreditService
import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.contractapi.entity.payment.PaymentDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/credit")
class CreditController(
    private val creditService: CreditService,
    @Value("\${logger.default}")
    private val logName: String = ""
) {

    private val log: Logger = LoggerFactory.getLogger(logName)

    @PostMapping("/")
    fun takeCredit(@RequestBody creditDto: CreditDto): ResponseEntity<Mono<CreditDto>> {
        log.info("Trying to take credit $creditDto")
        return ResponseEntity.ok(creditService.save(creditDto))
            .also {log.info("Response ${it.body} was sent")}
    }

    @PostMapping("/pay")
    fun pay(@RequestBody paymentDto: PaymentDto): ResponseEntity<Mono<CreditDto>> {
        log.info("Trying to apply payment $paymentDto to credit ${paymentDto.creditId}")
        return ResponseEntity.ok(creditService.pay(paymentDto))
            .also {log.info("Response ${it.body} was sent")}
    }

}