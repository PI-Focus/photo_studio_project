package pi.focus.server.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.core.service.api.IUserService;

import java.util.UUID;

/**
 * Основной контроллер приложения, отвечающий за навигацию по сайту и аутентификацию.
 * Обрабатывает запросы к информационным страницам, каталогам и системе регистрации.
 */
@Controller
@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
public class HomeController {
    private final IStaticDataService staticDataService;
    private final IRoomService roomService;
    private final IUserService userService;
    private final IEquipmentService equipmentService;
    private final IPhotographerService photographerService;

    /** Минимально допустимая длина логина пользователя */
    private static final int MIN_LOGIN_LENGTH = 4;
    /** Минимально допустимая длина пароля пользователя */
    private static final int MIN_PASSWORD_LENGTH = 8;

    /** Конструктор с необходимыми зависимостями */
    public HomeController(
            IStaticDataService staticDataService,
            IRoomService roomService,
            IUserService userService,
            IEquipmentService equipmentService,
            IPhotographerService photographerService 
    ) {
        this.staticDataService = staticDataService;
        this.roomService = roomService;
        this.userService = userService;
        this.equipmentService = equipmentService;
        this.photographerService = photographerService;
    }

    /** 
     * Отображает главную страницу с информацией о студии.
     * @param model модель для передачи текстовых данных в шаблон
     * @return путь к шаблону главной страницы
     */
    @GetMapping()
    public String getInfo(Model model) {
        model.addAttribute("info", staticDataService.getInfo());
        return "pages/info";
    }

    /**
     * Отображает список доступных фотозалов.
     * 
     * @param model модель для списка залов
     * @return путь к шаблону каталога залов
     */
    @GetMapping("/photorooms")
    public String getPhotorooms(Model model) {
        model.addAttribute("photorooms", roomService.getPhotoroomsContext());
        return "pages/photorooms";
    }

    /** 
     * Отображает информацию о конкретном фотозале.
     * @param model модель для данных зала
     * @param id уникальный идентификатор зала (UUID)
     * @return путь к шаблону конкретного зала
     */
    @GetMapping("/photorooms/{id}")
    public String getPhotoroom(Model model, @PathVariable UUID id) {
        model.addAttribute("photoroom", roomService.getConcretePhotoroomContext(id));
        return "pages/concrete-photoroom";
    }

    /**
     * Отображает список всего доступного оборудования.
     * @param model модель для списка оборудования
     * @return путь к шаблону каталога оборудования
     */
    @GetMapping("/equipment")
    public String getEquipment(Model model) {
        model.addAttribute("equipment", equipmentService.getEquipmentContext());
        return "pages/equipment";
    }

    /**
     * Отображает список всех доступных фотографов
     * @param model модель для списка фотографов
     * @return путь к шаблону каталога фотографов
     */
    @GetMapping("/photographers")
    public String getPhotographers(Model model) {
        model.addAttribute("photographers", photographerService.getEquipmentContext());
        return "pages/photographers";
    }

    /**
     * Отображает профиль пользователя.
     * @param model модель для профиля пользователя
     * @return путь к шаблону профиля пользователя
     */
    @GetMapping("/profile")
    public String getProfile(Model model) {
        return "pages/login";
    }

    /**
     * Обрабатывает запрос на отображение страницы входа.
     * Если пользователь уже авторизован, то перенаправляет его на предыдущую страницу или главную
     * @param request текущий HTTP запрос
     * @param session текущая сессия пользователя
     * @param error флаг ошибки авторизации 
     * @param model модель для аттрибутов ошибки
     * @param authentication объект текущей аутентификации 
     * @return путь к странице логина или редирект
     */
    @GetMapping("/login")
    public String getLogin(
            HttpServletRequest request,
            HttpSession session,
            @RequestParam(required = false) String error,
            Model model,
            Authentication authentication
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            String previousUri = (String) session.getAttribute("previousUri");
            if (previousUri != null) {
                session.removeAttribute("previousUri");
                return "redirect:" + previousUri;
            }
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("loginError", true);
        } else {
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.contains("/login") && !referer.contains("/registration")) {
                session.setAttribute("previousUri", referer);
            }
        }

        return "pages/login";
    }

    /**
     * Обрабатывает запрос на отображение страницы регистрации пользователя
     * @param model модель для формы регистрации пользователя
     * @return путь к шаблону регистрации пользователя
     */
    @GetMapping("/registration")
    public String getRegistration(Model model) {
        return "pages/registration";
    }
    
    /**
     * Обрабатывает форму регистрации нового пользователя.
     * Выполняет валидацию данных, проверку на уникальность логина и автоматический вход.
     * 
     * @param login желаемый логин
     * @param password пароль
     * @param confirmPassword подтверждение пароля
     * @param redirectAttributes атрибуты для передачи ошибок валидации через редирект
     * @return редирект на главную при успехе или обратно на регистрацию при ошибке
     */
    @PostMapping("/registration")
    public String createUser(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpSession session
    ) {
        if (!validateRegistration(login, password, confirmPassword, redirectAttributes)) {
            return "redirect:/registration?error";
        }

        User user = new User(null, login, null, null, password, UserRole.USER);
        if (!userService.createUser(user)) {
            redirectAttributes.addFlashAttribute("error",
                    "Пользователь с таким именем уже существует");
            return "redirect:/registration?error";
        }
        return autoLogin(login, password, request, session);
    }

    /**
     * Выполняет программный вход пользователя в систему после регистрации. 
     * @param login логина пользователя
     * @param password пароль пользователя
     * @param request текущий запрос для вызова метода login
     * @param session сессия для получения  сохраненного URI перехода
     * @return редирект на целевую страницу 
     */
    private String autoLogin(
            String login,
            String password,
            HttpServletRequest request,
            HttpSession session
    ) {
        try {
            request.login(login, password);
        } catch (ServletException e) {
            return "redirect:/login";
        }

        String previousUrl = (String) session.getAttribute("previousUri");
        if (previousUrl != null) {
            session.removeAttribute("previousUri");
            return "redirect:" + previousUrl;
        }
        return "redirect:/";
    }

    /**
     * Валидирует данные формы регистрации. 
     * Проверяет совпадение паролей, их длину и корректность логина.
     * @param login логин пользователя
     * @param password пароль пользователя
     * @param confirmPassword подтверждение пароля
     * @param redirectAttributes атрибуты для передачи ошибок валидации через редирект
     * @return true, если данные валидны, иначе false
     */
    private boolean validateRegistration(
            String login,
            String password,
            String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Пароли не совпадают");
            return false;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            redirectAttributes.addFlashAttribute("error",
                    "Минимальная длина пароля должна быть 8 символов");
            return false;
        }
        if (!login.matches("^[a-z0-9_-]+$")) {
            redirectAttributes.addFlashAttribute("error", "Логин может содержать " +
                    "только строчные латинские буквы, цифры, символы нижнего подчеркивания и дефиса");
            return false;
        }
        if (!login.matches(".*[a-z].*")) {
            redirectAttributes.addFlashAttribute("error",
                    "Логин должен содержать латинские буквы");
            return false;
        }
        if (login.length() < MIN_LOGIN_LENGTH) {
            redirectAttributes.addFlashAttribute("error",
                    "Минимальная длина логина должна быть 4 символов");
            return false;
        }
        return true;
    }
}
