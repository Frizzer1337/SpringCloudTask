package com.frizzer.contractapi.entity

enum class CreditStatus {
    CREATED,
    APPROVED,
    EXPIRED,
    NOT_APPROVED,
    NEED_HUMAN_APPROVE,
    NEED_COLLECTOR,
    SEND_TO_COLLECTOR,
    CREDIT_PAYED
}
