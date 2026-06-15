package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
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
