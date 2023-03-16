package com.frizzer.contractapi.entity.client

import org.bson.types.ObjectId

data class ClientDto(
    var id: String = ObjectId().toString(),
    var name: String,
    var surname: String,
    var phone: String,
    var salary: Int,
    var socialCredit: Double
)

fun ClientDto.fromDto() = Client(
    id = id,
    name = name,
    surname = surname,
    phone = phone,
    salary = salary,
    socialCredit = socialCredit
)