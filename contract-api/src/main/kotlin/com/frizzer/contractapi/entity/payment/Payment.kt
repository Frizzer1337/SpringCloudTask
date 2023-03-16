package com.frizzer.contractapi.entity.payment

import org.bson.types.ObjectId

data class Payment(
    var id: String = ObjectId().toString(),
    var payment: Int,
    var creditId: String,
    var status: PaymentStatus = PaymentStatus.PENDING
)