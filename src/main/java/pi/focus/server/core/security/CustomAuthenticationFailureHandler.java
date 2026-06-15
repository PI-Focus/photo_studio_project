package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * Обработчик ошибок аутентификации. 
 * Используется для кастомизации поведения системы при неудачной попытке входа.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * Вызывается при возникновении исключения во время процесса входа.
     * Перенаправляет пользователя обратно на страницу логина с параметром ошибки.
     * 
     * @param request текущий HTTP-запрос
     * @param response текущий HTTP-ответ
     * @param exception исключение, возникшее при аутентификации
     * @throws IOException при ошибках ввода-вывода или редиректа
     */
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        if (exception instanceof BadCredentialsException) {
            response.sendRedirect("/login?error");
            return;
        }
        if (exception.getCause() instanceof UsernameNotFoundException) {
            response.sendRedirect("/login?error");
            return;
        }

        response.sendRedirect("/login?error");
    }
}
