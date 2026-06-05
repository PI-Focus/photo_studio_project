package pi.focus.server.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.exception.AutoLoginException;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.core.service.api.IUserService;
import pi.focus.server.service.context.mocks.ExampleContextMock;

import java.util.List;


@Controller
public class HomeController {
    public IStaticDataService staticDataService;
    public IRoomService roomService;
    public IUserService userService;
    public PasswordEncoder passwordEncoder;

    public HomeController(
            IStaticDataService staticDataService,
            IRoomService roomService,
            IUserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.staticDataService = staticDataService;
        this.roomService = roomService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/test-db") // TODO: delete this
    public ResponseEntity<List<Room>> getTest() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping()
    public String getInfo(Model model) {
        model.addAttribute("info", staticDataService.getInfo());
        return "pages/info";
    }

    @GetMapping("/photorooms")
    public String getPhotorooms(Model model) {
        model.addAttribute("photorooms", new ExampleContextMock());
        return "pages/photorooms";
    }

    @GetMapping("/equipment")
    public String getEquipment(Model model) {
        model.addAttribute("equipment", new ExampleContextMock());
        return "pages/equipment";
    }

    @GetMapping("/photographers")
    public String getPhotographers(Model model) {
        model.addAttribute("photographers", new ExampleContextMock());
        return "pages/photographers";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("login", new ExampleContextMock());
        return "pages/login";
    }

    @GetMapping("/registration")
    public String getSignup(Model model) {
        model.addAttribute("login", new ExampleContextMock());
        return "pages/login";
    }

    @PostMapping("/registration")
    public String createUser(@RequestParam String login, @RequestParam String password, HttpServletRequest request) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, login, encodedPassword, UserRole.USER);
        userService.createUser(user);

        try {
            request.login(login, password);
        } catch (ServletException e) {
            throw new AutoLoginException("Autologin failed", e);
        }
        return "redirect:/";
    }

    @GetMapping("/example")
    public String getExample(Model model) {
        model.addAttribute("example", new ExampleContextMock());
        return "pages/example";
    }
}
