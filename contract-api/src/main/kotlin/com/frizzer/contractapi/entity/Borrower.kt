package com.frizzer.contractapi.entity


data class Borrower(
    var id: String,
    var name: String,
    var surname: String,
    var phone: String,
    var salary: Int,
    var socialCredit: Double
)
