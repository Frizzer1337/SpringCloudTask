package com.frizzer.approveapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ApproveApiApplication

fun main(args: Array<String>) {
    runApplication<ApproveApiApplication>(*args)
}
