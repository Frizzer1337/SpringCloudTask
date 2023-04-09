package com.frizzer.clientapi.controller

import com.frizzer.clientapi.service.ClientService
import com.frizzer.contractapi.entity.client.ClientDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/client")
class ClientController(
    private val clientService: ClientService
) {

    @PostMapping("/")
    fun register(@RequestBody clientDto: ClientDto): ResponseEntity<Mono<ClientDto>> {
        println("hi")
        return ResponseEntity.ok(clientService.save(clientDto))
    }

    @GetMapping("/{id}")
    fun findClientById(@PathVariable("id") id: Int): ResponseEntity<Mono<ClientDto>> {
        return ResponseEntity.ok(clientService.findClientById(id))
    }

    @GetMapping("/")
    fun findAll(): ResponseEntity<Flux<ClientDto>> {
        return ResponseEntity.ok(clientService.findAll())
    }

}