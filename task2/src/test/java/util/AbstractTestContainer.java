package util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestContainer {
    private static final Logger logger = LoggerFactory.getLogger(AbstractTestContainer.class);

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("user_db")
            .withUsername("korn")
            .withPassword("postgres");

    @BeforeAll
    static void setUp() {
        System.setProperty("hibernate.connection.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("hibernate.connection.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("hibernate.connection.password", POSTGRES_CONTAINER.getPassword());
    }

    @AfterAll
    static void tearDown() {
        logger.info("PostgreSQL Testcontainer stopped!");
    }
}