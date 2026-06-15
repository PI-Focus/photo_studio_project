package pi.focus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения фотостудии «Фокус».
 * Служит точкой входа для запуска Spring Boot сервера.
 * Данный класс инициализирует контекст приложения, выполняет сканирование
 * компонентов
 * и запускает встроенный веб-сервер.
 */
@SpringBootApplication
public final class ServerApplication {
    /**
     * Приватный конструктор для предотвращения создания экземпляров основного
     * класса.
     */
    private ServerApplication() {
    }

    /**
     * Основной метод, запускающий выполнение приложения.
     * 
     * @param args аргументы командной строки, передаваемые при старте приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
