package com.frizzer.contractapi.entity.payment

data class Payment(
    var id: String,
    var payment: Int,
    var creditId: String,
    var status: PaymentStatus
)

fun Payment.toDto() = PaymentDto(
    id = id,
    payment = payment,
    creditId = creditId,
    status = status
)