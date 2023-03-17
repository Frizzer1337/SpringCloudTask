package com.frizzer.approveapi.service

import com.frizzer.approveapi.repository.CreditRepository
import com.frizzer.contractapi.entity.client.Client
import com.frizzer.contractapi.entity.client.fromDto
import com.frizzer.contractapi.entity.credit.*
import com.frizzer.contractapi.entity.exception.PaymentApproveException
import com.frizzer.contractapi.entity.payment.PaymentDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    @Value(value = "\${credit.modifier.salary}")
    private val salaryModifier: Double = 1.0

    @Value(value = "\${credit.modifier.socialCredit}")
    private val socialCreditModifier: Double = 1.0

    @Value(value = "\${credit.modifier.creditBalance}")
    private val creditBalanceModifier: Double = 1.0

    @Value(value = "\${credit.rate.auto}")
    private val approveRate: Double = 1.0

    @Value(value = "\${credit.rate.human}")
    private val humanApproveRate: Double = 1.0

    @Value(value = "\${kafka.topic.check}")
    private val creditCheckTopic: String = ""

    @Transactional
    open fun save(creditDto: CreditDto): Mono<CreditDto> {
        return creditRepository.save(creditDto.fromDto())
            .then(clientService.findClientById(creditDto.clientId)
                .flatMap { approve(creditDto, it.fromDto()) })
            .flatMap { sendCreditCheckEventKafka(it.toDto()).thenReturn(it.toDto()) }
    }

    @Transactional
    open fun pay(paymentDto: PaymentDto): Mono<CreditDto> {
        return creditRepository
            .findCreditById(paymentDto.creditId)
            .flatMap { credit ->
                credit.creditBalance = credit.creditBalance - paymentDto.payment
                credit.penalty = credit.penalty - paymentDto.payment
                credit.lastPaymentDate = LocalDateTime.now().toString()
                creditRepository.save(credit)
            }
            .map { it.toDto() }
            .doOnNext {
                throw PaymentApproveException("Error while changing credit")
            }
    }


    private fun sendCreditCheckEventKafka(credit: CreditDto): Mono<SenderResult<Void>> {
        return kafkaTemplate.send(
            creditCheckTopic, CreditCheckEvent(credit.id, credit.creditStatus)
        ).doOnSuccess { result ->
            log.info(
                "sent {} offset: {}",
                CreditCheckEvent(credit.id, credit.creditStatus),
                result.recordMetadata().offset()
            )
        }
    }

    private fun approve(
        credit: CreditDto,
        client: Client,
    ): Mono<Credit> {
        val creditRate: Double =
            (client.socialCredit * socialCreditModifier + client.salary * salaryModifier - credit.creditBalance * creditBalanceModifier)
        return approveByCreditRate(creditRate, credit)

    }

    private fun approveByCreditRate(
        creditRate: Double,
        credit: CreditDto,
    ): Mono<Credit> {
        credit.creditStatus = getCreditStatus(creditRate)
        return creditRepository.save(credit.fromDto())
    }

    private fun getCreditStatus(
        creditRate: Double
    ) = if (creditRate > approveRate) {
        CreditStatus.APPROVED
    } else if (creditRate > humanApproveRate) {
        CreditStatus.NEED_HUMAN_APPROVE
    } else {
        CreditStatus.NOT_APPROVED
    }
}