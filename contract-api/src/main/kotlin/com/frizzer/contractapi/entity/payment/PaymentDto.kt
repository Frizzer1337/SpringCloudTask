package com.frizzer.contractapi.entity.payment


data class PaymentDto(
    var id: Int,
    var payment: Int,
    var creditId: Int,
    var status: PaymentStatus = PaymentStatus.PENDING
)

fun PaymentDto.fromDto() = Payment(
    id = id,
    payment = payment,
    creditId = creditId,
    status = status
)