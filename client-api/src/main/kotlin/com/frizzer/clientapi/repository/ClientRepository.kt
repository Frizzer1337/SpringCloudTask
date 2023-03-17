package com.frizzer.clientapi.repository

import com.frizzer.contractapi.entity.client.Client
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ClientRepository : ReactiveMongoRepository<Client, String> {
    fun findClientById(id: String): Mono<Client>
}