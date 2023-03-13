package com.frizzer.approvemicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ApproveMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<ApproveMicroserviceApplication>(*args)
}
