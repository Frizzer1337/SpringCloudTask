package com.frizzer.clientapi

import com.frizzer.clientapi.config.AbstractTestcontainersIntegrationTest
import com.frizzer.clientapi.entity.ClientJson
import com.frizzer.clientapi.repository.ClientRepository
import com.frizzer.contractapi.entity.client.ClientDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.test.StepVerifier
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(classes = [ClientApiApplication::class])
class ClientApiTest(
    @Autowired var webClient: WebTestClient,
    @Autowired var clientRepository: ClientRepository
) : AbstractTestcontainersIntegrationTest() {

    @Test
    fun testDatabaseIsInit() {
        clientRepository.findAll().`as`(StepVerifier::create).expectNextCount(5).verifyComplete()
    }

    @Test
    fun testFindAll() {
        webClient.get().uri("/client/").accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk
    }

    @Test
    fun testFindById() {
        webClient.get().uri("/client/{id}", "1").accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk
    }

    @Test
    fun testNotFoundById() {
        webClient.get().uri("/client/{id}", "10").accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun testNotExistingCommand() {
        webClient.get().uri("/cliend/").accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun testCreateEmptyClient() {
        webClient.post().uri("/client/").accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun testCreateClient() {
        var saveId = 0
        webClient.post().uri("/client/").body(
            BodyInserters.fromValue(
                ClientJson(
                    name = "Danik",
                    surname = "Korobkov",
                    phone = "123456789",
                    salary = 1000,
                    socialCredit = 102.6
                )
            )
        ).exchange()
            .expectStatus().isOk
            .returnResult(ClientDto::class.java).responseBody
            .`as`(StepVerifier::create)
            .expectNextMatches {
                saveId = it.id
                it.id > 0
            }
            .verifyComplete()

        assertNotNull(clientRepository.findClientById(saveId))

    }

}