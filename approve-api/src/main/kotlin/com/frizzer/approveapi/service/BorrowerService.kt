package com.frizzer.approveapi.service

import com.frizzer.contractapi.entity.borrower.Borrower
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(value = "borrower-api", url = "http://localhost:7000/borrower")
@Service
interface BorrowerService {
    @RequestMapping(method = [RequestMethod.GET], value = ["/byId/{id}"])
    fun findBorrowerById(@PathVariable("id") borrowerId: String): Mono<Borrower>
}
