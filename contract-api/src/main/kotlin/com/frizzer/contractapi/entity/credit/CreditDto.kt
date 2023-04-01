package com.frizzer.contractapi.entity.credit

import java.time.LocalDateTime

data class CreditDto(
    val id: String?,
    val lastPaymentDate: String = LocalDateTime.now().toString(),
    val creditBalance: Int,
    val penalty: Int,
    val clientId: String,
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