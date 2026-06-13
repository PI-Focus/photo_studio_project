package pi.focus.server.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import pi.focus.server.service.context.mocks.ConcretePhotoroomContextMock;

import java.util.UUID;


@Controller
public class HomeController {
    public IStaticDataService staticDataService;
    public IRoomService roomService;
    public IUserService userService;
    public PasswordEncoder passwordEncoder;
    public IEquipmentService equipmentService;
    public IPhotographerService photographerService;

    public HomeController(
            IStaticDataService staticDataService,
            IRoomService roomService,
            IUserService userService, 
            PasswordEncoder passwordEncoder,
            IEquipmentService equipmentService,
            IPhotographerService photographerService
    ) {
        this.staticDataService = staticDataService;
        this.roomService = roomService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.equipmentService = equipmentService;
        this.photographerService = photographerService;
    }

    @GetMapping()
    public String getInfo(Model model) {
        model.addAttribute("info", staticDataService.getInfo());
        return "pages/info";
    }

    @GetMapping("/photorooms")
    public String getPhotorooms(Model model) {
        model.addAttribute("photorooms", roomService.getPhotoroomsContext());
        return "pages/photorooms";
    }

    @GetMapping("/photorooms/{id}")
    public String getPhotoroom(Model model, @PathVariable UUID id) {
        model.addAttribute("photoroom", new ConcretePhotoroomContextMock(id.toString().substring(0, 8)));
        return "pages/concrete-photoroom";
    }

    @GetMapping("/equipment")
    public String getEquipment(Model model) {
        model.addAttribute("equipment", equipmentService.getEquipmentContext());
        return "pages/equipment";
    }

    @GetMapping("/photographers")
    public String getPhotographers(Model model) {
        model.addAttribute("photographers", photographerService.getEquipmentContext());
        return "pages/photographers";
    }

    @GetMapping("/login")
    public String getLogin(Model model, HttpSession session, @ModelAttribute("previousURI") String previousURI) {
        if (previousURI != null && !previousURI.contains("/registration") && !previousURI.contains("/login")) {
            session.setAttribute("previousUri", previousURI);
        }
        return "pages/login";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        return "pages/registration";
    }
    public String getSignup(Model model) {
        return "pages/login";
    }

    @PostMapping("/registration")
    public String createUser(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpSession session
    ) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Пароли не совпадают");
            return "redirect:/registration?error";
        }

        if (!login.matches("^[a-z0-9_-]+$")) {
            redirectAttributes.addFlashAttribute("error",
            "Логин может содержать только строчные латинские буквы, цифры, символы нижнего подчеркивания и дефиса");
            return "redirect:/registration?error";
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, login, encodedPassword, UserRole.USER);
        if (!userService.createUser(user)) {
            redirectAttributes.addFlashAttribute("error",
                    "Пользователь с таким именем уже существует");
            return "redirect:/registration?error";
        }
        return autoLogin(login, password, request, session);
    }

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
}
