package com.frizzer.contractapi.entity.client

data class ClientDto(
    val id: Int,
    val name: String,
    val surname: String,
    val phone: String,
    val salary: Int,
    val socialCredit: Double
)

fun ClientDto.fromDto() = Client(
    id = id,
    name = name,
    surname = surname,
    phone = phone,
    salary = salary,
    socialCredit = socialCredit
)