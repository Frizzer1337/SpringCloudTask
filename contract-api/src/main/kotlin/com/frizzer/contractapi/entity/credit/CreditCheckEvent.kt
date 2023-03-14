package com.frizzer.contractapi.entity.credit

import java.time.LocalDate

data class CreditCheckEvent(
    var id: String,
    var creditStatus: CreditStatus,
    var timestamp: String = LocalDate.now().toString()
)