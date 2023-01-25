package com.frizzer.borrowermicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BorrowerMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<BorrowerMicroserviceApplication>(*args)
}
