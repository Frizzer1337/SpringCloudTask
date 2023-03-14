package com.frizzer.approveapi.service

import com.frizzer.approveapi.repository.CreditRepository
import com.frizzer.contractapi.entity.borrower.Borrower
import com.frizzer.contractapi.entity.credit.Credit
import com.frizzer.contractapi.entity.credit.CreditCheckEvent
import com.frizzer.contractapi.entity.credit.CreditStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val kafkaConsumer: ReactiveKafkaConsumerTemplate<String, Credit>,
    private val borrowerService: BorrowerService,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, CreditCheckEvent>
) {

    private val log: Logger = LoggerFactory.getLogger(CreditService::class.java)

    //kafkaListener
    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceiving(): Flux<Credit> {
        return kafkaConsumer
            .receiveAutoAck()
            .flatMap { record ->
                borrowerService.findBorrowerById(record.value().borrowerId)
                    .flatMap { borrower -> approve(record.value(), borrower) }
            }
            .flatMap { credit ->
                sendCreditCheckEventKafka(credit).thenReturn(credit)
            }
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

    private fun approve(credit: Credit, borrower: Borrower): Mono<Credit> {
        val socialCreditModifier = 1.5
        val salaryModifier = 10.0
        val creditBalanceModifier = 0.2
        val creditRate: Double = (borrower.socialCredit * socialCreditModifier + borrower.salary
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