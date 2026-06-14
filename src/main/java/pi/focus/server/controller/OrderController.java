package pi.focus.server.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IRoomService;
import java.time.format.DateTimeParseException;


@Controller
@RequestMapping("/order")
public class OrderController {
    private final IRoomService roomService;
    private final IEquipmentService equipmentService;

    public OrderController(IRoomService roomService, IEquipmentService equipmentService) {
        this.roomService = roomService;
        this.equipmentService = equipmentService;
    }

    @GetMapping("/calendar/{id}")
    public ResponseEntity<ICalendar> getPhotorooms(@PathVariable String id, @RequestParam String date) {
        UUID uuid;
        LocalDate localDate;
        try {
            uuid = UUID.fromString(id);
            localDate = LocalDate.parse(date);
        } catch (DateTimeParseException | IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        ICalendar calendar = roomService.getRoomCalendar(uuid, localDate);
        if (calendar == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getEquipment() {
        return ResponseEntity.ok().body(equipmentService.getEquipment());
    }
//
//    @GetMapping("/photographers")
//    public ResponseEntity<> getPhotorooms() {
//
//        return ResponseEntity.ok();
//    }
}
