package com.frizzer.borrowerapi.controller


import com.frizzer.borrowerapi.service.BorrowerService
import com.frizzer.contractapi.entity.borrower.Borrower
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/borrower")
class BorrowerController(
    private val borrowerService: BorrowerService
) {

    @PostMapping("/register")
    fun register(@RequestBody borrower: Borrower): ResponseEntity<Mono<Borrower>> {
        return ResponseEntity.ok(borrowerService.register(borrower))
    }

    @GetMapping("/byId/{id}")
    fun findBorrowerById(@PathVariable("id") id: String): ResponseEntity<Mono<Borrower>> {
        return ResponseEntity.ok(borrowerService.findBorrowerById(id))
    }
}