package com.frizzer.clientapi.service

import com.frizzer.clientapi.repository.ClientRepository
import com.frizzer.contractapi.entity.client.ClientDto
import com.frizzer.contractapi.entity.client.fromDto
import com.frizzer.contractapi.entity.client.toDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ClientService(
    private val clientRepository: ClientRepository,
) {
    fun save(clientDto: ClientDto): Mono<ClientDto> {
        return clientRepository
            .save(clientDto.fromDto())
            .map { it.toDto() }
    }

    fun findClientById(id: String): Mono<ClientDto> {
        return clientRepository.findClientById(id).map { it.toDto() }
    }
}