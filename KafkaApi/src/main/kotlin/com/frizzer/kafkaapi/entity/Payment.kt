package com.frizzer.kafkaapi.entity

data class Payment(
    var id: String,
    val payment: Int,
    val creditId: String
)