package com.frizzer.clientapi.service

import com.frizzer.clientapi.repository.ClientRepository
import com.frizzer.contractapi.entity.client.ClientDto
import com.frizzer.contractapi.entity.client.fromDto
import com.frizzer.contractapi.entity.client.toDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
open class ClientService(
    private val clientRepository: ClientRepository,
) {
    @Transactional
    open fun save(clientDto: ClientDto): Mono<ClientDto> {
        return clientRepository
            .save(clientDto.fromDto())
            .map { it.toDto() }
    }

    @Transactional
    open fun findClientById(id: Int): Mono<ClientDto> {
        return clientRepository
            .findClientById(id)
            .switchIfEmpty(
                ResponseStatusException(HttpStatus.NOT_FOUND, "ClientFeignClient not found").toMono()
            )
            .map { it.toDto() }
    }

    open fun findAll(): Flux<ClientDto> {
        return clientRepository
            .findAll()
            .map { it.toDto() }
    }
}