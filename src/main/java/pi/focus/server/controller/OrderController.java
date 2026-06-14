package pi.focus.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.service.context.mocks.CalendarMock;


@Controller
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/calendar")
    public ResponseEntity<ICalendar> getPhotorooms() {
        return ResponseEntity.ok(new CalendarMock());
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
