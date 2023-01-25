package com.frizzer.kafkaapi.entity

data class CollectorEvent (
    val id: String? = null,
    val creditStatus: CreditStatus? = null,
    val timestamp: String? = null
)