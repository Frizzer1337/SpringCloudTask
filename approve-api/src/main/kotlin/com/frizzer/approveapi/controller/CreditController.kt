package com.frizzer.approveapi.controller

import com.frizzer.approveapi.service.CreditService
import com.frizzer.contractapi.entity.credit.Credit
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

    @PostMapping("/takeCredit")
    fun takeCredit(@RequestBody credit: Credit): ResponseEntity<Mono<Credit>> {
        return ResponseEntity.ok(creditService.save(credit))
    }


}