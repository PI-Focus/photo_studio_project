package pi.focus.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.AvoidDuplicateLiterals"})
@Controller
public class HomeController {
    private final PageData data = new PageData(
            5,
            List.of(1, 2, 3, 4, 5),
            "Very cool text!",
            List.of("Text number one", "Text number two", "Text number three", "Text number four", "Text number five"),
            "/images/placeholder.png",
            Collections.nCopies(5, "/images/placeholder.png")
    );

    @GetMapping()
    public String getInfo(Model model) {
        model.addAttribute("context", data);
        return "info";
    }

    @GetMapping("/photorooms")
    public String getPhotorooms(Model model) {
        model.addAttribute("context", data);
        return "photorooms";
    }

    @GetMapping("/equipment")
    public String getEquipment(Model model) {
        model.addAttribute("context", data);
        return "equipment";
    }

    @GetMapping("/photographers")
    public String getPhotographers(Model model) {
        model.addAttribute("context", data);
        return "photographers";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("context", data);
        return "login";
    }
}
