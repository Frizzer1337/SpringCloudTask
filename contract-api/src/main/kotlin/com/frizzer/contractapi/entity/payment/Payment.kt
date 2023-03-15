package com.frizzer.contractapi.entity.payment

data class Payment(
    var id: String,
    var payment: Int,
    var creditId: String,
    var status : PaymentStatus = PaymentStatus.PENDING
)