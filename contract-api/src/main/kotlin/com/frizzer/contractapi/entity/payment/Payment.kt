package com.frizzer.contractapi.entity.payment

import org.springframework.data.util.ProxyUtils
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "payment")
data class Payment(
    @Id
    @org.springframework.data.annotation.Id
    var id: String?,
    var payment: Int,
    var creditId: String,
    var status: PaymentStatus
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(
                other
            )
        ) return false
        other as Payment

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , payment = $payment , creditId = $creditId , status = $status )"
    }
}

fun Payment.toDto() = PaymentDto(
    id = id,
    payment = payment,
    creditId = creditId,
    status = status
)