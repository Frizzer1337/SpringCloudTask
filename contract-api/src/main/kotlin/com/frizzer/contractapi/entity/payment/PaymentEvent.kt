package com.frizzer.contractapi.entity.payment

import java.time.LocalDate

data class PaymentEvent(
    var id: String,
    var currentStatus: PaymentStatus,
    var timestamp: String = LocalDate.now().toString()
)