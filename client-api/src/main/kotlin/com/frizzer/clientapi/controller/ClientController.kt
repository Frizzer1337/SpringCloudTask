package com.frizzer.clientapi.controller

import com.frizzer.clientapi.service.ClientService
import com.frizzer.contractapi.entity.client.ClientDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/client")
class ClientController(
    private val clientService: ClientService,
    @Value("\${logger.default}")
    private val logName: String = ""
) {

    private val log: Logger = LoggerFactory.getLogger(logName)

    @PostMapping("/")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun register(@RequestBody clientDto: ClientDto): ResponseEntity<Mono<ClientDto>> {
        log.info("Registering client: $clientDto")
        return ResponseEntity.ok(clientService.save(clientDto))
            .also { log.info("Response ${it.body} was sent") }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun findClientById(@PathVariable("id") id: Int): ResponseEntity<Mono<ClientDto>> {
        log.info("Finding client by id: $id")
        return ResponseEntity.ok(clientService.findClientById(id))
            .also { log.info("Response ${it.body} was sent") }
    }

    @GetMapping("/")
    @CrossOrigin(origins = ["\${angular.origins}"])
    fun findAll(): ResponseEntity<Flux<ClientDto>> {
        log.info("Finding all clients")
        return ResponseEntity.ok(clientService.findAll())
            .also { log.info("Response ${it.statusCode} was sent") }
    }

}