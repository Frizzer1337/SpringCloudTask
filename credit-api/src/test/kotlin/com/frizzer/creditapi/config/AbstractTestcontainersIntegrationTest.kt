package com.frizzer.creditapi.config

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

abstract class AbstractTestcontainersIntegrationTest {

    companion object {

        private val mockWebServer: MockWebServer = MockWebServer()

        private val kafkaContainer: KafkaContainer =
            KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.1"))

        private val mysql: MySQLContainer<*> =
            MySQLContainer(DockerImageName.parse("mysql:5.7"))
                .apply {
                    withDatabaseName("db")
                    withUsername("user")
                    withPassword("user")
                }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("reactive-feign.client.client-api.url", Companion::mockWebServerUrl)
            registry.add("spring.r2dbc.url", Companion::r2dbcUrl)
            registry.add("spring.r2dbc.username", mysql::getUsername)
            registry.add("spring.r2dbc.password", mysql::getPassword)
            registry.add("kafka.bootstrap", kafkaContainer::getBootstrapServers)
        }

        private fun r2dbcUrl(): String {
            return "r2dbc:mysql://${mysql.host}:${mysql.getMappedPort(MySQLContainer.MYSQL_PORT)}/${mysql.databaseName}"
        }

        private fun mockWebServerUrl(): String {
            return mockWebServer.url("/client").toString()
        }


        @JvmStatic
        @BeforeAll
        internal fun setUp() {

            val response = MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200)
                .setBody(
                    """
                    {
                        "id": 1,
                        "name": "Danik",
                        "surname": "Suzdalev",
                        "phone": "37511111",
                        "salary": 100,
                        "socialCredit": 10.2
                    }
                    """.trimIndent()
                )

            mockWebServer.enqueue(response)

            mockWebServer.start(7000)
            mysql.start()
            kafkaContainer.start()
        }

    }
}