package com.frizzer.contractapi.entity.client

import org.bson.types.ObjectId

data class Client(
    var id: String = ObjectId().toString(),
    var name: String,
    var surname: String,
    var phone: String,
    var salary: Int,
    var socialCredit: Double
)
