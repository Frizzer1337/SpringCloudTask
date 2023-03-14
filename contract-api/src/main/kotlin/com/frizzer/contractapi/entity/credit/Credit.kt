package com.frizzer.contractapi.entity.credit

data class Credit(
    var id: String,
    var lastPaymentDate: String,
    var creditBalance: Int,
    var penalty: Int,
    var borrowerId: String,
    var creditType: Int,
    var creditStatus: CreditStatus,
)