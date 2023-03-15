package com.frizzer.approveapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling
import reactivefeign.spring.config.EnableReactiveFeignClients


@SpringBootApplication
@EnableFeignClients
@EnableReactiveFeignClients
@EnableScheduling
open class CreditApiApplication

fun main(args: Array<String>) {
    runApplication<CreditApiApplication>(*args)
}
