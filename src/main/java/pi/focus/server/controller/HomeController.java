package pi.focus.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pi.focus.server.core.domain.Room;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.service.context.mocks.ExampleContextMock;

import java.util.List;


@Controller
public class HomeController {
    public IStaticDataService staticDataService;
    public IRoomService roomService;

    public HomeController(
            IStaticDataService staticDataService,
            IRoomService roomService
    ) {
        this.staticDataService = staticDataService;
        this.roomService = roomService;
    }

    @GetMapping("/test-db")
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

    @GetMapping("/example")
    public String getExample(Model model) {
        model.addAttribute("example", new ExampleContextMock());
        return "pages/example";
    }
}
