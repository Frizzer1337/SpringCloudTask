package com.frizzer.contractapi.entity

import java.time.LocalDate

data class CreditPayedEvent(val credit: Credit) {
    val id: String = credit.id
    val creditStatus: CreditStatus = credit.creditStatus
    val timestamp: String = LocalDate.now().toString()
}