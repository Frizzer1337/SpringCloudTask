package com.frizzer.creditapi

import com.frizzer.contractapi.entity.credit.CreditDto
import com.frizzer.creditapi.config.AbstractTestcontainersIntegrationTest
import com.frizzer.creditapi.entity.CreditJson
import com.frizzer.creditapi.entity.PaymentJson
import com.frizzer.creditapi.repository.CreditRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.test.StepVerifier
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = [CreditApiApplication::class])

class CreditApiTest(
    @Autowired var webClient: WebTestClient,
    @Autowired var creditRepository: CreditRepository
) : AbstractTestcontainersIntegrationTest() {

    @Test
    fun testDatabaseIsInit() {
        creditRepository.findAll().`as`(StepVerifier::create).expectNextCount(5).verifyComplete()
    }

    @Test
    fun testEmptySave() {
        webClient.post()
            .uri("/credit/")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun testSaveCredit() {
        var saveId = 0
        webClient.post()
            .uri("/credit/")
            .body(
                BodyInserters.fromValue(
                    CreditJson(
                        creditBalance = 100,
                        penalty = 0,
                        clientId = 1
                    )
                )
            )
            .exchange()
            .expectStatus()
            .isOk
            .returnResult(CreditDto::class.java).responseBody
            .`as`(StepVerifier::create)
            .expectNextMatches {
                saveId = it.id
                it.id > 0
            }
            .verifyComplete()

        assertNotNull(creditRepository.findById(saveId))
    }

    @Test
    fun testPayInCredit() {
        val saveId = 1
        val payment = 100
        var balance = 0

        creditRepository.findById(saveId).`as`(StepVerifier::create)
            .expectNextMatches {
                balance = it.creditBalance
                it.creditBalance > 0
            }
            .verifyComplete()

        webClient.post().uri("/credit/pay").body(
            BodyInserters.fromValue(
                PaymentJson(
                    payment = payment,
                    creditId = saveId
                )
            )
        )
            .exchange()
            .expectStatus()
            .isOk
            .returnResult(CreditDto::class.java).responseBody
            .`as`(StepVerifier::create)
            .expectNextMatches {
                it.id > 0
            }
            .verifyComplete()

        creditRepository.findById(saveId).`as`(StepVerifier::create)
            .expectNextMatches {
                balance -= it.creditBalance
                it.creditBalance > 0
            }
            .verifyComplete()

        assertNotNull(creditRepository.findById(saveId))
        assertEquals(payment, balance)
    }

}