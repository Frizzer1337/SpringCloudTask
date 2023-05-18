package com.frizzer.clientapi.config

import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

abstract class AbstractTestcontainersIntegrationTest {

    companion object {

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
            registry.add("spring.r2dbc.url", Companion::r2dbcUrl)
            registry.add("spring.r2dbc.username", mysql::getUsername)
            registry.add("spring.r2dbc.password", mysql::getPassword)
        }

        private fun r2dbcUrl(): String {
            return "r2dbc:mysql://${mysql.host}:${mysql.getMappedPort(MySQLContainer.MYSQL_PORT)}/${mysql.databaseName}"
        }


        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            mysql.start()
        }

    }
}