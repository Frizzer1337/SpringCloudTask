package com.frizzer.contractapi.entity.payment

import org.bson.types.ObjectId

data class PaymentDto(
    var id: String = ObjectId().toString(),
    var payment: Int,
    var creditId: String,
    var status: PaymentStatus = PaymentStatus.PENDING
)

fun PaymentDto.fromDto() = Payment(
    id = id,
    payment = payment,
    creditId = creditId,
    status = status
)