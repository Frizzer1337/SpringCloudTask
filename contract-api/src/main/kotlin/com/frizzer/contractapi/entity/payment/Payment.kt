package com.frizzer.contractapi.entity.payment

data class Payment(
    var id: String,
    val payment: Int,
    val creditId: String
)