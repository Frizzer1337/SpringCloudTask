package com.frizzer.kafkaapi.entity

data class Payment(
    var id: String? = null,
    val payment: Int = 0,
    val creditId: String? = null
)