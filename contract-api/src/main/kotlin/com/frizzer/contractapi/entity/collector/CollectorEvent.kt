package com.frizzer.contractapi.entity.collector

import com.frizzer.contractapi.entity.credit.CreditStatus
import java.time.LocalDate

data class CollectorEvent(
    val id: String,
    val creditStatus: CreditStatus,
    val timestamp: String? = LocalDate.now().toString()
)