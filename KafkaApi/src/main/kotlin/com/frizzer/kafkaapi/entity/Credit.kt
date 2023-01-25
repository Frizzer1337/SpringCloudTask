package com.frizzer.kafkaapi.entity

data class Credit (
    val id: String,
    val lastPaymentDate: String,
    val creditBalance: Int,
    val penalty: Int,
    val borrowerId : String,
    val creditType: Int,
    val creditStatus: CreditStatus,
) {
    constructor() : this("1","1",0,0,"1",0, CreditStatus.NOT_APPROVED)
}