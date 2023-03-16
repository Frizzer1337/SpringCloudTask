package com.frizzer.approveapi.service

import com.frizzer.approveapi.repository.CreditRepository
import com.frizzer.contractapi.entity.client.Client
import com.frizzer.contractapi.entity.credit.*
import com.frizzer.contractapi.entity.exception.PaymentApproveException
import com.frizzer.contractapi.entity.payment.Payment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import java.time.LocalDateTime

@Service
open class CreditService(
    private val creditRepository: CreditRepository,
    private val clientService: ClientService,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, CreditCheckEvent>
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CreditService::class.java)
    }

    open fun save(creditDto: CreditDto): Mono<CreditDto> {
        return creditRepository.save(creditDto.fromDto())
            .then(clientService
                .findClientById(creditDto.clientId)
                .flatMap { approve(creditDto, it) })
            .flatMap { sendCreditCheckEventKafka(it.toDto()).thenReturn(it.toDto()) }
    }

    open fun pay(payment: Payment): Mono<Credit> {
        return creditRepository.findCreditById(payment.creditId)
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance - payment.payment
                credit.penalty = credit.penalty - payment.payment
                credit.lastPaymentDate = LocalDateTime.now().toString()
                creditRepository.save(credit)
            }.doOnError { throw PaymentApproveException("Error while changing credit") }
    }

    private fun sendCreditCheckEventKafka(credit: CreditDto): Mono<SenderResult<Void>> {
        return kafkaTemplate.send(
            "CREDIT_CHECKED",
            CreditCheckEvent(credit.id, credit.creditStatus)
        )
            .doOnSuccess { result ->
                log.info(
                    "sent {} offset: {}",
                    CreditCheckEvent(credit.id, credit.creditStatus),
                    result.recordMetadata().offset()
                )
            }
    }

    private fun approve(credit: CreditDto, client: Client): Mono<Credit> {
        val socialCreditModifier = 1.5
        val salaryModifier = 10.0
        val creditBalanceModifier = 0.2
        val creditRate: Double = (client.socialCredit * socialCreditModifier + client.salary
                * salaryModifier - credit.creditBalance * creditBalanceModifier)
        return approveByCreditRate(creditRate, credit)

    }

    private fun approveByCreditRate(creditRate: Double, credit: CreditDto): Mono<Credit> {
        val approveRate = 120.0
        val humanApproveRate = 100.0
        credit.creditStatus = getCreditStatus(creditRate, approveRate, humanApproveRate)
        return creditRepository.save(credit.fromDto())
    }

    private fun getCreditStatus(
        creditRate: Double,
        approveRate: Double,
        humanApproveRate: Double
    ) = if (creditRate > approveRate) {
        CreditStatus.APPROVED
    } else if (creditRate > humanApproveRate) {
        CreditStatus.NEED_HUMAN_APPROVE
    } else {
        CreditStatus.NOT_APPROVED
    }
}