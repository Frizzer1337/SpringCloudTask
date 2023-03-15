package com.frizzer.clientapi.service

import com.frizzer.clientapi.repository.ClientRepository
import com.frizzer.contractapi.entity.client.Client
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ClientService(private val clientRepository: ClientRepository) {
    fun register(client: Client): Mono<Client> {
        return clientRepository.save(client)
    }

    fun findBorrowerById(id: String): Mono<Client> {
        return clientRepository.findBorrowerById(id)
    }
}