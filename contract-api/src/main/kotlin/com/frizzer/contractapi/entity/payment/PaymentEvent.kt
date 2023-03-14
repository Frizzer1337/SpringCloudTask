package com.frizzer.contractapi.entity.payment

import com.frizzer.contractapi.entity.credit.Credit
import java.time.LocalDate

data class PaymentEvent(val credit: Credit) {
    val id: String = credit.id
    val currentBalance: Int = credit.creditBalance
    val timestamp: String = LocalDate.now().toString()
}