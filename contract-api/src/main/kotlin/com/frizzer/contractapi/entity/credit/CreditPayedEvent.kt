package com.frizzer.contractapi.entity.credit

import java.time.LocalDate

data class CreditPayedEvent(
    var id: String,
    var creditStatus: CreditStatus,
    val timestamp: String = LocalDate.now().toString()
)