package pi.focus.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("PMD.AtLeastOneConstructor")
@Controller
public class HomeController {
    @GetMapping()
    public String getIndex(Model model) {
        String message = "Very important text!";
        model.addAttribute("message", message);

        return "index";
    }
}
