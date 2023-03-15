package com.frizzer.clientapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class ClientApiApplication

fun main(args: Array<String>) {
    runApplication<ClientApiApplication>(*args)
}
