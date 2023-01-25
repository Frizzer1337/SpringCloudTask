package com.frizzer.kafkaapi.entity;


data class Borrower(
    var id: String,
    var name: String,
    var surname: String,
    var phone: String,
    var salary: Int,
    var socialCredit: Double
){
    constructor() : this("1","1","1","1",0,0.0)
}
