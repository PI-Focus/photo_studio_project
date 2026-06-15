package pi.focus.server.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.core.service.api.IStaticDataService;

/**
 * Контроллер-советник
 * Используется для автоматического добавления общих аттрибутов во все модели отображения.
 * Данные становятся доступны во всех HTML-шаблонах приложения.
 */
@ControllerAdvice
public class GlobalTemplateControllerAdvice {
    
    /** Сервис для доступа к базовым статическим данным студии */
    private final IStaticDataService staticDataService;

    /**
     * Конструктор для инициализации сервиса статических данных 
     * @param staticDataService сервис статических данных
     */
    GlobalTemplateControllerAdvice(IStaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    /**
     * Добавляет текущий URI запроса в модель.
     * Позволяет в HTML-шаблонах определять активную страницу. 
     * @param request текущий HTTP запрос
     * @return строка с текущим URI
     */
    @ModelAttribute("currentURI")
    public String currentURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * Добавляет URI предыдущей страницы в модель.
     * @param request текущий HTTP запрос
     * @return строка с путем предыдущей страницы или null
     */
    @ModelAttribute("previousURI")
    public String previousURI(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    /**
     * Предоставляет базовый контекст сайта во все шаблоны.
     * Данные доступны в HTML под переменной base.
     * @return объект базового контекста студии
     */
    @ModelAttribute("base")
    public IBaseContext base() {
        return staticDataService.getBaseContext();
    }
}
