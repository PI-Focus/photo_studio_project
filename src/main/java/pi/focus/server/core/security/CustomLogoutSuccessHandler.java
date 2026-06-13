package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
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
