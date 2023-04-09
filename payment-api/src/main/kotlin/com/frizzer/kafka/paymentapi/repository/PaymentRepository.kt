package com.frizzer.kafka.paymentapi.repository

import com.frizzer.contractapi.entity.payment.Payment
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface PaymentRepository : R2dbcRepository<Payment, Int>