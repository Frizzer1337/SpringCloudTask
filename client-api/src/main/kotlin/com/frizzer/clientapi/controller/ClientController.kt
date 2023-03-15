package com.frizzer.clientapi.controller

import com.frizzer.clientapi.service.ClientService
import com.frizzer.contractapi.entity.client.Client
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/client")
class ClientController(
    private val clientService: ClientService
) {

    @PostMapping()
    fun register(@RequestBody client: Client): ResponseEntity<Mono<Client>> {
        return ResponseEntity.ok(clientService.register(client))
    }

    @GetMapping("/{id}")
    fun findBorrowerById(@PathVariable("id") id: String): ResponseEntity<Mono<Client>> {
        return ResponseEntity.ok(clientService.findBorrowerById(id))
    }
}