package pi.focus.server.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;

import java.util.List;

/**
 * Главный конфигурационный класс безопасности приложения.
 * Настраивает цепочку фильтров безопасности правила доступа к эндпоинтам,
 * процесс авторизации через форму и алгоритмы шифрования паролей.
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    private static final int MIN_LOGIN_LENGTH = 4;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Конфигурация HTTP безопасности.
     * Определяет публичные и защищенные ресурсы, настройки формы входа и выхода.
     * 
     * @param http объект для настройки безопасности
     * @return сконфигурированная цепочка фильтров
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable) // TODO: delete this row
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/images/**", "/js/**", "/fonts/**", "/docs/**", "/favicon.ico").permitAll()
                .requestMatchers("/", "/photorooms", "/photorooms/{id}", "/equipment", "/photographers", "/login", "/login?*", "/registration").permitAll()
                .requestMatchers("/profile").hasRole(UserRole.USER.name())
            ).formLogin(form -> form
                .loginPage("/login").permitAll()
                .usernameParameter("login")
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())
            ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }

    /**
     * Бин для шифрования паролей с использованием алгоритма BCrypt.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Пользовательский сервис для поиска и загрузки данных пользователя при входе.
     * Поддерживает три формата логина: телефон, email или классический логин.
     * 
     * @return реализация UserDetailsService
     * @throws UsernameNotFoundException если формат логина неверен или пользователь не найден
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            UserEntity user;
            if (login.matches("^8\\d{10}$")) {
                user = userRepository.findByPhoneNumber(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Неверный логин или пароль"));
            } else if (login.matches("^[^@]+@[^@]+$")) {
                user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Неверный логин или пароль"));
            } else if (login.matches("^[a-z0-9_-]+$") && login.matches(".*[a-z].*")
                    && login.length() >= MIN_LOGIN_LENGTH) {
                user = userRepository.findByLogin(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Неверный логин или пароль"));
            } else {
                throw new UsernameNotFoundException("Неверный логин или пароль");
            }
            if (user.getRole() == UserRole.ADMIN) {
                throw new UsernameNotFoundException("Неверный логин или пароль");
            }
            List<SimpleGrantedAuthority> roles = List.of(user.getRole().toAuthority());

            return new User(
                user.getLogin(),
                user.getPassword(),
                roles
            );
        };
    }

    /**
     * Создает бин обработчика успешной аутентификации.
     * Использует CustomAuthenticationSuccessHandler для управления редиректами после входа.
     * 
     * @return объект обработчика успешного входа
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    /**
     * Создает бин обработчика неудачной аутентификации.
     * Использует CustomAuthenticationFailureHandler для возврата на страницу логина с ошибкой.
     * 
     * @return объект обработчика ошибок входа
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /**
     * Создает бин обработчика успешного выхода из системы.
     * Использует CustomLogoutSuccessHandler для возврата пользователя на исходную страницу.
     * 
     * @return объект обработчика завершения сессии
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}

