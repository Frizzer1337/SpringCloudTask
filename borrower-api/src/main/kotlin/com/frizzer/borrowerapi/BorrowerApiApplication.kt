package com.frizzer.borrowerapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class BorrowerApiApplication

fun main(args: Array<String>) {
    runApplication<BorrowerApiApplication>(*args)
}
