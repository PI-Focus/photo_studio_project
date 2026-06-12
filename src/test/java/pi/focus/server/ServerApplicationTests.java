//package pi.focus.server;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.test.context.ActiveProfiles;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@Testcontainers
//@SpringBootTest
//@ActiveProfiles("test")
//class ServerApplicationTests {
//
//    @Container
//    @ServiceConnection
//    static final PostgreSQLContainer<?> POSTGRES = initContainer();
//
//    private static PostgreSQLContainer<?> initContainer() {
//        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
//        return new PostgreSQLContainer<>("postgres:latest")
//                .withDatabaseName(env.get("TEST_DB_NAME"))
//                .withUsername(env.get("TEST_DB_USER"))
//                .withPassword(env.get("TEST_DB_PASSWORD"));
//    }
//
//}
