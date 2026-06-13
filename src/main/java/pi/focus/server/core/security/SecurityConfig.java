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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    private static final int MIN_LOGIN_LENGTH = 4;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable) // TODO: delete this row
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
                .requestMatchers("/", "/photorooms", "/equipment", "/photographers", "/registration").permitAll()
                .anyRequest().authenticated()
            ).formLogin(form -> form
                .loginPage("/login").permitAll()
                .usernameParameter("login")
                .successHandler(authenticationSuccessHandler())
            ).logout(logout -> logout
                .logoutUrl("/logout").permitAll()
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            UserEntity user;
            if (login.matches("^8\\d{10}$")) {
                user = userRepository.findByPhoneNumber(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким номером телефона не найден: " + login));
            } else if (login.matches("^[^@]+@[^@]+$")) {
                user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь с такой email не найден: " + login));
            } else if (login.matches("^[a-z0-9_-]+$") && login.matches(".*[a-z].*")
                    && login.length() >= MIN_LOGIN_LENGTH) {
                user = userRepository.findByLogin(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким login не найден: " + login));
            } else {
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

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}

