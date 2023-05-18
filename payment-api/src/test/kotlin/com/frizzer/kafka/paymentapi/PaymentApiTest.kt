package com.frizzer.kafka.paymentapi

import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.kafka.paymentapi.config.AbstractTestcontainersIntegrationTest
import com.frizzer.kafka.paymentapi.entity.PaymentJson
import com.frizzer.kafka.paymentapi.repository.PaymentRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.test.StepVerifier
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = [PaymentApiApplication::class])
class PaymentApiTest(
    @Autowired var webClient: WebTestClient,
    @Autowired var paymentRepository: PaymentRepository
) : AbstractTestcontainersIntegrationTest() {

    @Test
    fun testDatabaseIsInit() {
        paymentRepository.findAll().`as`(StepVerifier::create).expectNextCount(5).verifyComplete()
    }

    @Test
    fun testPaymentEmptySave() {
        webClient.post()
            .uri("/payment/")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun testPaymentSave() {
        var saveId = 0
        webClient.post()
            .uri("/payment/")
            .body(
                BodyInserters.fromValue(
                    PaymentJson(
                        payment = 100,
                        creditId = 1
                    )
                )
            )
            .exchange()
            .expectStatus().isOk
            .returnResult(CreditDto::class.java).responseBody
            .`as`(StepVerifier::create)
            .expectNextMatches {
                saveId = it.id
                it.id > 0
            }
            .verifyComplete()

        assertNotNull(paymentRepository.findById(saveId))
    }


}