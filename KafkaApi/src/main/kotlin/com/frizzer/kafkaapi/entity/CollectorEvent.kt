package com.frizzer.kafkaapi.entity

import java.time.LocalDate

data class CollectorEvent (
    val id: String? = null,
    val creditStatus: CreditStatus? = null,
    val timestamp: String? = LocalDate.now().toString()
)