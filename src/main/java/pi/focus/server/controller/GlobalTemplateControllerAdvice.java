package pi.focus.server.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.core.service.api.IStaticDataService;

@ControllerAdvice
public class GlobalTemplateControllerAdvice {
    
    private final IStaticDataService staticDataService;

    GlobalTemplateControllerAdvice(IStaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @ModelAttribute("currentURI")
    public String currentURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("base")
    public IBaseContext base() {
        return staticDataService.getBaseContext();
    }
}
