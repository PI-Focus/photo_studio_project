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
                .defaultSuccessUrl("/")
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
        return username -> {
            UserEntity user = userRepository.findByLoginIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
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

