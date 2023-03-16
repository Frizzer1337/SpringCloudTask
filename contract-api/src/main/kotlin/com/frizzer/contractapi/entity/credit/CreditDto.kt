package com.frizzer.contractapi.entity.credit

import org.bson.types.ObjectId

data class CreditDto(
    var id: String = ObjectId().toString(),
    var lastPaymentDate: String,
    var creditBalance: Int,
    var penalty: Int,
    var clientId: String,
    var creditStatus: CreditStatus = CreditStatus.CREATED
)

fun CreditDto.fromDto() = Credit(
    id = id,
    lastPaymentDate = lastPaymentDate,
    creditBalance = creditBalance,
    penalty = penalty,
    clientId = clientId,
    creditStatus = creditStatus
)