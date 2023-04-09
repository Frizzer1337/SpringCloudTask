package com.frizzer.contractapi.entity.client

import org.springframework.data.util.ProxyUtils
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "client")
data class Client(
    @Id
    @org.springframework.data.annotation.Id
    var id: Int,
    var name: String,
    var surname: String,
    var phone: String,
    var salary: Int,
    var socialCredit: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(other)
        ) return false
        other as Client

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , surname = $surname , phone = $phone , salary = $salary , socialCredit = $socialCredit )"
    }
}


fun Client.toDto() = ClientDto(
    id = id,
    name = name,
    surname = surname,
    phone = phone,
    salary = salary,
    socialCredit = socialCredit
)
