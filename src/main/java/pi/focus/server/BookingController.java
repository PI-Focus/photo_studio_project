package pi.focus.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("PMD.AtLeastOneConstructor")
@Controller
public class BookingController {
    @GetMapping()
    public String getMessage(Model model) {
        String message = "Very important text!";
        model.addAttribute("message", message);
        return "message";
    }
}
