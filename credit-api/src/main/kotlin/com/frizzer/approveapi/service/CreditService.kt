package com.frizzer.approveapi.service

import com.frizzer.approveapi.repository.CreditRepository
import com.frizzer.contractapi.entity.client.Client
import com.frizzer.contractapi.entity.credit.Credit
import com.frizzer.contractapi.entity.credit.CreditCheckEvent
import com.frizzer.contractapi.entity.credit.CreditStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult

@Service
open class CreditService(
    private val creditRepository: CreditRepository,
    private val clientService: ClientService,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, CreditCheckEvent>
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CreditService::class.java)
    }

    @Transactional
    open fun save(credit: Credit): Mono<Credit> {
        return creditRepository.save(credit)
            .then(clientService
                .findBorrowerById(credit.borrowerId)
                .flatMap { approve(credit, it) })
            .flatMap { sendCreditCheckEventKafka(it).thenReturn(it) }
    }

    private fun sendCreditCheckEventKafka(credit: Credit): Mono<SenderResult<Void>> {
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

    private fun approve(credit: Credit, client: Client): Mono<Credit> {
        val socialCreditModifier = 1.5
        val salaryModifier = 10.0
        val creditBalanceModifier = 0.2
        val creditRate: Double = (client.socialCredit * socialCreditModifier + client.salary
                * salaryModifier - credit.creditBalance * creditBalanceModifier)
        return approveByCreditRate(creditRate, credit)

    }

    private fun approveByCreditRate(creditRate: Double, credit: Credit): Mono<Credit> {
        val approveRate = 120.0
        val humanApproveRate = 100.0
        credit.creditStatus = getCreditStatus(creditRate, approveRate, humanApproveRate)
        return creditRepository.save(credit)
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