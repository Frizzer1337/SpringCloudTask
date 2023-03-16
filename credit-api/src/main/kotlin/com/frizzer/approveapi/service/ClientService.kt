package com.frizzer.approveapi.service

import com.frizzer.contractapi.entity.client.ClientDto
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(value = "client-api", url = "http://localhost:7000/client")
@Service
interface ClientService {
    @RequestMapping(method = [RequestMethod.GET], value = ["/{id}"])
    fun findClientById(@PathVariable("id") clientId: String): Mono<ClientDto>
}
