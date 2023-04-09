package com.frizzer.clientapi.service

import com.frizzer.clientapi.repository.ClientRepository
import com.frizzer.contractapi.entity.client.ClientDto
import com.frizzer.contractapi.entity.client.fromDto
import com.frizzer.contractapi.entity.client.toDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
            .map { it.toDto() }
    }

    open fun findAll() : Flux<ClientDto> {
        return clientRepository
            .findAll()
            .map { it.toDto() }
    }
}