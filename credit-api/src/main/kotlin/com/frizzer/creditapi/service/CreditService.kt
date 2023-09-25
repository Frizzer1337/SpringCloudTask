package com.frizzer.creditapi.service

import com.frizzer.contractapi.entity.client.Client
import com.frizzer.contractapi.entity.client.fromDto
import com.frizzer.contractapi.entity.credit.*
import com.frizzer.contractapi.entity.payment.PaymentDto
import com.frizzer.creditapi.client.ClientFeignClient
import com.frizzer.creditapi.repository.CreditRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDateTime

@Service
open class CreditService(
    private val creditRepository: CreditRepository,
    private val feignClient: ClientFeignClient,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, CreditCheckEvent>
) {

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

    open fun findAll(): Flux<CreditDto> {
        return creditRepository.findAll().map { it.toDto() }
    }

    open fun findById(id: Int): Mono<CreditDto> {
        return creditRepository.findById(id)
            .switchIfEmpty(
                ResponseStatusException(HttpStatus.NOT_FOUND, "Credit not found").toMono()
            )
            .map { it.toDto() }
    }

    @Transactional
    open fun save(creditDto: CreditDto): Mono<CreditDto> {
        return feignClient.findClientById(creditDto.clientId).switchIfEmpty(
            ResponseStatusException(
                HttpStatus.NOT_FOUND, "ClientFeignClient not found"
            ).toMono()
        ).flatMap { approve(creditDto.fromDto(), it.fromDto()) }
            .flatMap { sendCreditCheckEventKafka(it.toDto()).thenReturn(it.toDto()) }
    }

    @Transactional
    open fun pay(paymentDto: PaymentDto): Mono<CreditDto> {
        return creditRepository.findById(paymentDto.creditId).switchIfEmpty(
            ResponseStatusException(HttpStatus.NOT_FOUND, "Credit not found").toMono()
        ).flatMap { credit ->
            credit.creditBalance = credit.creditBalance - paymentDto.payment
            credit.penalty = credit.penalty - paymentDto.payment
            credit.lastPaymentDate = LocalDateTime.now().toString()
            credit.toMono()
        }.flatMap { credit ->
            creditRepository.save(credit)
        }.map { it.toDto() }
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
        credit: Credit,
        client: Client,
    ): Mono<Credit> {
        val creditRate: Double =
            (client.socialCredit * socialCreditModifier + client.salary * salaryModifier - credit.creditBalance * creditBalanceModifier)
        return approveByCreditRate(creditRate, credit)

    }

    private fun approveByCreditRate(
        creditRate: Double,
        credit: Credit,
    ): Mono<Credit> {
        credit.creditStatus = getCreditStatus(creditRate)
        return creditRepository.save(credit)
    }

    private fun getCreditStatus(creditRate: Double) = when {
        creditRate > approveRate -> CreditStatus.APPROVED
        creditRate > humanApproveRate -> CreditStatus.NEED_HUMAN_APPROVE
        else -> CreditStatus.NOT_APPROVED
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CreditService::class.java)
    }
}