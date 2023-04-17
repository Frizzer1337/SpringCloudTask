package com.frizzer.approveapi.client

import com.frizzer.contractapi.entity.client.ClientDto
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(name = "client-api", url = "\${reactive-feign.client.client-api.url}")
@Service
interface ClientFeignClient {
    @RequestMapping(method = [RequestMethod.GET], value = ["/{id}"])
    fun findClientById(@PathVariable("id") clientId: Int): Mono<ClientDto>
}
