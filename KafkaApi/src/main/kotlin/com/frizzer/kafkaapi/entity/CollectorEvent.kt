package com.frizzer.kafkaapi.entity

import java.time.LocalDate

data class CollectorEvent(
    val id: String,
    val creditStatus: CreditStatus,
    val timestamp: String? = LocalDate.now().toString()
)