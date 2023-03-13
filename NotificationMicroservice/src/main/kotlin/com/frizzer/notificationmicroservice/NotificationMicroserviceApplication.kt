package com.frizzer.notificationmicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class NotificationMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<NotificationMicroserviceApplication>(*args)
}
