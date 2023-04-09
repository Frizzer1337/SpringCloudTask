package com.frizzer.contractapi.entity.credit

import org.springframework.data.util.ProxyUtils
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Table
data class Credit(
    @Id
    @org.springframework.data.annotation.Id
    var id: Int,
    var lastPaymentDate: String,
    var creditBalance: Int,
    var penalty: Int,
    var clientId: Int,
    @Enumerated(EnumType.STRING)
    var creditStatus: CreditStatus
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(
                other
            )
        ) return false
        other as Credit

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , lastPaymentDate = $lastPaymentDate , creditBalance = $creditBalance , penalty = $penalty , clientId = $clientId , creditStatus = $creditStatus )"
    }
}

fun Credit.toDto() = CreditDto(
    id = id,
    lastPaymentDate = lastPaymentDate,
    creditBalance = creditBalance,
    penalty = penalty,
    clientId = clientId,
    creditStatus = creditStatus
)
