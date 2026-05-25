package pi.focus.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@Profile("test")
class ServerApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("TEST_DB_NAME")
            .withUsername("TEST_DB_USER")
            .withPassword("TEST_DB_PASSWORD");

    @Test
    void contextLoads() {
        //now it's a dummy test, I hope it will be a real test someday...
    }

}
