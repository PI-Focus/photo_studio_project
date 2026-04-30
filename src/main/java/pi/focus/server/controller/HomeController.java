package pi.focus.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pi.focus.server.service.context.mocks.ExampleContextMock;
import pi.focus.server.service.context.mocks.InfoContextMock;
import pi.focus.server.service.context.mocks.PhotoroomsContextMock;


@Controller
public class HomeController {
    public HomeController() {}

    @GetMapping()
    public String getInfo(Model model) {
        model.addAttribute("info", new InfoContextMock());
        return "pages/info";
    }

    @GetMapping("/photorooms")
    public String getPhotorooms(Model model) {
        model.addAttribute("photorooms", new PhotoroomsContextMock());
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

    @GetMapping("/example")
    public String getExample(Model model) {
        model.addAttribute("example", new ExampleContextMock());
        return "pages/example";
    }
    
}
