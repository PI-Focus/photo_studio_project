package pi.focus.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pi.focus.server.context.mocks.ExampleContextMock;
import pi.focus.server.context.mocks.InfoContextMock;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.AvoidDuplicateLiterals"})
@Controller
public class HomeController {
    // TODO: Add switch logic between mocks and realization

    @GetMapping()
    public String getInfo(Model model) {
        model.addAttribute("info", new InfoContextMock());
        return "info";
    }

    @GetMapping("/photorooms")
    public String getPhotorooms(Model model) {
        model.addAttribute("photorooms", new ExampleContextMock());
        return "photorooms";
    }

    @GetMapping("/equipment")
    public String getEquipment(Model model) {
        model.addAttribute("equipment", new ExampleContextMock());
        return "equipment";
    }

    @GetMapping("/photographers")
    public String getPhotographers(Model model) {
        model.addAttribute("photographers", new ExampleContextMock());
        return "photographers";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("login", new ExampleContextMock());
        return "login";
    }

    @GetMapping("/example")
    public String getExample(Model model) {
        model.addAttribute("example", new ExampleContextMock());
        return "example";
    }
    
}
