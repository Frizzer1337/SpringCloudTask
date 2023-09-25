package com.frizzer.contractapi.entity.credit

import java.time.LocalDate

data class CreditPayedEvent(
    val id: Int,
    val creditStatus: CreditStatus,
    val timestamp: String = LocalDate.now().toString()
)