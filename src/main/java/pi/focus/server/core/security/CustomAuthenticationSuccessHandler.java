package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Objects;

/**
 * Обработчик успешной аутентификации.
 * Обеспечивает редирект: возвращает пользователя на ту страницу, 
 * с которой он пришел до авторизации.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * Вызывается после успешного входа пользователя.
     * Проверяет сессию на наличие сохраненного URI предыдущей страницы.
     * 
     * @param request текущий запрос
     * @param response текущий ответ
     * @param authentication объект с данными об авторизованном пользователе
     * @throws IOException при ошибках редиректа
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        HttpSession session = request.getSession(false);
        String previousUri = null;
        if (session != null) {
            Object previousUriObj = session.getAttribute("previousUri");
            if (previousUriObj != null) {
                previousUri = (String) previousUriObj;
                session.removeAttribute("previousUri");
            }
        }
        response.sendRedirect(Objects.requireNonNullElse(previousUri, "/"));
    }
}
