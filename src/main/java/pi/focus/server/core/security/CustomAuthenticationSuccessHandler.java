package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Objects;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
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
