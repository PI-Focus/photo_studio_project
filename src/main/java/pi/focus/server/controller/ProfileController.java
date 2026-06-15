package pi.focus.server.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IProfileOrders;
import pi.focus.server.api.models.IReservation;
import pi.focus.server.core.security.CustomUserDetails;
import pi.focus.server.core.service.api.IUserService;
import pi.focus.server.service.models.ProfileOrdersDto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/profile")
@Transactional
@SuppressWarnings({"PMD.ConfusingTernary"})
public class ProfileController {
    private final IUserService userService;

    public ProfileController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/orders")
    public String getEquipment(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session
    ) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            return redirectPrevious(session);
        }
        if (!uuid.equals(userDetails.userId())) {
            return redirectPrevious(session);
        } else {
            return "pages/profile";
        }
    }

    @GetMapping("/{id}/orders/calendar")
    public ResponseEntity<IProfileOrders> getEquipment(
            @PathVariable String id,
            @RequestParam String date,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session
    ) {
        UUID uuid;
        LocalDate localDate;
        try {
            uuid = UUID.fromString(id);
            localDate = LocalDate.parse(date);
        } catch (DateTimeParseException | IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        if (localDate.isAfter(LocalDate.now().plusDays(35))) {
            return ResponseEntity.badRequest().build();
        }
        ICalendar calendar = userService.getUserCalendar(uuid, localDate);
        if (calendar == null) {
            return ResponseEntity.badRequest().build();
        }
        List<IReservation> reservationDtos = userService.getUserReservationDtos(uuid, localDate).stream().map(
                reservationDto -> (IReservation) reservationDto
        ).toList();
        return ResponseEntity.ok().body(new ProfileOrdersDto(
                userDetails.login(),
                calendar,
                reservationDtos
        ));
    }

    private String redirectPrevious(HttpSession session) {
        String previousUri = (String) session.getAttribute("previousUri");
        if (previousUri != null) {
            session.removeAttribute("previousUri");
            return "redirect:" + previousUri;
        }
        return "redirect:/";
    }
}
