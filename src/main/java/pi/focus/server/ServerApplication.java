package pi.focus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class ServerApplication {
    private ServerApplication() {}
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
