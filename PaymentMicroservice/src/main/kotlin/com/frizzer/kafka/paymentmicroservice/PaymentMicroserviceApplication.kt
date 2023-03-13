package com.frizzer.kafka.paymentmicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class PaymentMicroserviceApplication

fun main(args: Array<String>) {
    runApplication<PaymentMicroserviceApplication>(*args)
}
