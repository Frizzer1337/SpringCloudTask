package com.frizzer.borrowermicroservice.controller


import com.frizzer.borrowermicroservice.service.impl.BorrowerServiceImpl
import com.frizzer.borrowermicroservice.service.impl.CreditServiceImpl
import com.frizzer.kafkaapi.entity.Borrower
import com.frizzer.kafkaapi.entity.Credit
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/borrower")
class BorrowerController(
    private val borrowerService: BorrowerServiceImpl,
    private val creditService: CreditServiceImpl
) {

    @PostMapping("/register")
    fun register(@RequestBody borrower: Borrower): ResponseEntity<Mono<Borrower>> {
        return ResponseEntity.ok(borrowerService.register(borrower))
    }

    @PostMapping("/takeCredit")
    fun takeCredit(@RequestBody credit: Credit): ResponseEntity<Mono<Credit>> {
        return ResponseEntity.ok(creditService.takeCredit(credit))
    }
}