package pi.focus.server.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pi.focus.server.api.models.ICalendar;
import pi.focus.server.service.context.mocks.CalendarMock;


@Controller
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/calendar/{id}")
    public ResponseEntity<ICalendar> getCalendar(
            @PathVariable() UUID id,
            @RequestParam() @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (date.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        ICalendar calendarMock = new CalendarMock(date);

        return ResponseEntity.ok(calendarMock);
    }

//    @GetMapping("/equipment")
//    public ResponseEntity<> getPhotorooms() {
//
//        return ResponseEntity.ok();
//    }
//
//    @GetMapping("/photographers")
//    public ResponseEntity<> getPhotorooms() {
//
//        return ResponseEntity.ok();
//    }
}
