package com.frizzer.contractapi.entity.credit

import org.bson.types.ObjectId
data class Credit(
    var id: String = ObjectId().toString(),
    var lastPaymentDate: String,
    var creditBalance: Int,
    var penalty: Int,
    var borrowerId: String,
    var creditStatus: CreditStatus,
)