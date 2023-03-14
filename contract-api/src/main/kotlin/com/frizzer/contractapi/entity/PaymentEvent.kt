package com.frizzer.contractapi.entity

import java.time.LocalDate

data class PaymentEvent(val credit: Credit) {
    val id: String = credit.id
    val currentBalance: Int = credit.creditBalance
    val timestamp: String = LocalDate.now().toString()
}