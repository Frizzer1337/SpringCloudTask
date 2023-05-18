package com.frizzer.creditapi.controller

import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.contractapi.entity.payment.PaymentDto
import com.frizzer.creditapi.service.CreditService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/credit")
class CreditController(
    private val creditService: CreditService,
    @Value("\${logger.default}")
    private val logName: String = ""
) {

    private val log: Logger = LoggerFactory.getLogger(logName)

    @GetMapping("/")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun findAll(): ResponseEntity<Flux<CreditDto>> {
        return ResponseEntity.ok(creditService.findAll())
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun findById(@PathVariable("id") id: Int): ResponseEntity<Mono<CreditDto>> {
        return ResponseEntity.ok(creditService.findById(id))
    }

    @PostMapping("/")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun takeCredit(@RequestBody creditDto: CreditDto): ResponseEntity<Mono<CreditDto>> {
        log.info("Trying to take credit $creditDto")
        return ResponseEntity.ok(creditService.save(creditDto))
            .also { log.info("Response ${it.body} was sent") }
    }

    @PostMapping("/pay")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun pay(@RequestBody paymentDto: PaymentDto): ResponseEntity<Mono<CreditDto>> {
        log.info("Trying to apply payment $paymentDto to credit ${paymentDto.creditId}")
        return ResponseEntity.ok(creditService.pay(paymentDto))
            .also { log.info("Response ${it.body} was sent") }
    }

}