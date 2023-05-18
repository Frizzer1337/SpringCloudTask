package com.frizzer.kafka.paymentapi.controller

import com.frizzer.contractapi.entity.payment.PaymentDto
import com.frizzer.kafka.paymentapi.service.PaymentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/payment")
class PaymentController(
    private val paymentService: PaymentService,
    @Value("\${logger.default}")
    private val logName: String = ""
) {

    private val log: Logger = LoggerFactory.getLogger(logName)

    @GetMapping("/")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun findAll(): ResponseEntity<Flux<PaymentDto>> {
        return ResponseEntity.ok(paymentService.findAll())
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun findById(@PathVariable("id") id: Int): ResponseEntity<Mono<PaymentDto>> {
        return ResponseEntity.ok(paymentService.findById(id))
    }

    @PostMapping("/")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun pay(@RequestBody paymentDto: PaymentDto): ResponseEntity<Mono<PaymentDto>> {
        log.info("Trying to make payment $paymentDto")
        return ResponseEntity.ok(paymentService.pay(paymentDto))
            .also { log.info("Response ${it.body} was sent") }
    }
}