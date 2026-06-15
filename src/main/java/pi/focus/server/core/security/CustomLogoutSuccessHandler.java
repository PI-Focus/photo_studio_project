package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * Обработчик успешного выхода из системы.
 * Позволяет пользователю остаться на той же странице, где он нажал кнопку "Выход".
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    /**
     * Вызывается после завершения процесса логаута.
     * Использует заголовок Referer для определения страницы возврата.
     * 
     * @param request текущий запрос
     * @param response текущий ответ
     * @param authentication данные пользователя
     * @throws IOException при ошибках редиректа
     */
    @Override
    public void onLogoutSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        @Nullable Authentication authentication
    ) throws IOException {
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "/");
    }
}
