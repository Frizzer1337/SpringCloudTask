package com.frizzer.notificationapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class NotificationApiApplication

fun main(args: Array<String>) {
    runApplication<NotificationApiApplication>(*args)
}
