package com.frizzer.approvemicroservice.service.impl

import com.frizzer.approvemicroservice.repository.CreditRepository
import com.frizzer.kafkaapi.entity.Borrower
import com.frizzer.kafkaapi.entity.Credit
import com.frizzer.kafkaapi.entity.CreditCheckEvent
import com.frizzer.kafkaapi.entity.CreditStatus
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
class CreditServiceImpl(
    private val creditRepository: CreditRepository,
    private val kafkaConsumer: ReactiveKafkaConsumerTemplate<String, Credit>,
    private val borrowerService: BorrowerServiceImpl,
    private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, CreditCheckEvent>
) {

    private val log: Logger = LoggerFactory.getLogger(CreditServiceImpl::class.java)


    @EventListener(ApplicationStartedEvent::class)
    fun kafkaReceiving(): Flux<Credit> {
        return kafkaConsumer
            .receiveAutoAck()
            .flatMap { x ->
                borrowerService.findBorrowerById(x.value().borrowerId)
                    .doOnNext { borrower -> print(borrower) }
                    .flatMap { borrower -> approve(x.value(), borrower) }
                    .doOnNext { borrower -> print(borrower) }
            }
            .flatMap { credit ->
                kafkaTemplate
                    .send("CREDIT_CHECKED", CreditCheckEvent(credit.id, credit.creditStatus))
                    .doOnSuccess { result: SenderResult<Void> ->
                        log.info(
                            "sent {} offset: {}",
                            CreditCheckEvent(credit.id, credit.creditStatus),
                            result.recordMetadata().offset()
                        )
                    }
                    .thenReturn(credit)
            }
    }

    private fun approve(credit: Credit, borrower: Borrower): Mono<Credit> {
        val socialCreditModifier = 1.5
        val salaryModifier = 10.0
        val creditBalanceModifier = 0.2
        val creditRate: Double = (borrower.socialCredit * socialCreditModifier + borrower.salary
                * salaryModifier - credit.creditBalance * creditBalanceModifier)
        return approveByCreditRate(creditRate,credit)

    }

    private fun approveByCreditRate(creditRate: Double, credit: Credit): Mono<Credit> {
        val approveRate = 120.0
        val humanApproveRate = 100.0
        var creditStatus: CreditStatus = CreditStatus.NOT_APPROVED
        if (creditRate > approveRate) {
            creditStatus = CreditStatus.APPROVED
        } else if (creditRate > humanApproveRate) {
            creditStatus = CreditStatus.NEED_HUMAN_APPROVE
        }
        return creditRepository.changeStatus(credit, creditStatus)
    }
}