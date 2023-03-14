package com.frizzer.kafka.paymentapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class PaymentApiApplicaton

fun main(args: Array<String>) {
    runApplication<PaymentApiApplicaton>(*args)
}
