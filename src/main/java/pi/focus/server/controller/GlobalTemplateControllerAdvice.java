package pi.focus.server.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalTemplateControllerAdvice {
    
    @ModelAttribute("currentURI")
    public String currentURI(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
