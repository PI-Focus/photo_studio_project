package pi.focus.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.core.service.api.IStaticDataService;
import org.springframework.web.bind.annotation.PathVariable;
import pi.focus.server.service.context.mocks.ConcretePhotoroomContextMock;

import java.util.UUID;


@Controller
public class HomeController {
    public IStaticDataService staticDataService;
    public IRoomService roomService;
    public IEquipmentService equipmentService;
    public IPhotographerService photographerService;

    public HomeController(
            IStaticDataService staticDataService,
            IRoomService roomService,
            IEquipmentService equipmentService,
            IPhotographerService photographerService
    ) {
        this.staticDataService = staticDataService;
        this.roomService = roomService;
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
    public String getLogin(Model model) {
        return "pages/login";
    }

    @GetMapping("/example")
    public String getExample(Model model) {
        return "pages/example";
    }
}
