package com.frizzer.clientapi.repository

import com.frizzer.contractapi.entity.client.Client
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ClientRepository : R2dbcRepository<Client, String> {
    fun findClientById(id: String): Mono<Client>
}