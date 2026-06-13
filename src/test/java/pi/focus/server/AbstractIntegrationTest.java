package pi.focus.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")

public abstract class AbstractIntegrationTest {

    static final PostgreSQLContainer<?> POSTGRES = initContainer();

    static {
        POSTGRES.start();
        Runtime.getRuntime().addShutdownHook(new Thread(POSTGRES::stop));
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    private static PostgreSQLContainer<?> initContainer() {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
        return new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName(env.get("TEST_DB_NAME"))
                .withUsername(env.get("TEST_DB_USER"))
                .withPassword(env.get("TEST_DB_PASSWORD"));
    }
}