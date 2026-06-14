package pi.focus.server.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IOrder;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.models.EquipmentDto;
import pi.focus.server.service.models.OrderDto;
import pi.focus.server.service.models.OrderStatusDto;

import java.time.format.DateTimeParseException;


@Controller
@RequestMapping("/order")
@Transactional
public class OrderController {
    private final IRoomService roomService;
    private final IEquipmentService equipmentService;
    private final IPhotographerService photographerService;

    public OrderController(
            IRoomService roomService,
            IEquipmentService equipmentService,
            IPhotographerService photographerService
    ) {
        this.roomService = roomService;
        this.equipmentService = equipmentService;
        this.photographerService = photographerService;
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

    @GetMapping("/photographers")
    public ResponseEntity<List<Photographer>> getPhotoPhotographer(
            @RequestParam String start,
            @RequestParam String end
    ) {
        LocalDateTime fromTime;
        LocalDateTime toTime;
        try {
            fromTime = LocalDateTime.parse(start);
            toTime = LocalDateTime.parse(end);
        } catch (DateTimeParseException | IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
        Range<LocalDateTime> time = Range.closed(fromTime, toTime);
        return ResponseEntity.ok().body(photographerService.getPhotographersByTime(time));
    }

    @GetMapping("/current")
    public ResponseEntity<IOrderStatus> getCurrent(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        IOrderStatus orderStatus = (IOrderStatus) session.getAttribute("orderStatus");
        if (orderStatus == null) {
            orderStatus = new OrderStatusDto(
                    null,
                    new OrderDto(
                            null,
                            null,
                            null,
                            new ArrayList<>(),
                            0
                    )
            );
        }
        session.setAttribute("orderStatus", orderStatus);
        return ResponseEntity.ok().body(orderStatus);
    }

//    @PostMapping("/current")
//    public ResponseEntity<IOrderStatus> getCurrent(
//            @RequestBody IOrderStatus orderStatus,
//            HttpServletRequest request
//    ) {
//        HttpSession session = request.getSession(true);
//        Integer validateStatus = validateOrderStatus(orderStatus);
//        if (validateStatus == 0) {
//            session.setAttribute("orderStatus", orderStatus);
//            return ResponseEntity.ok().body(orderStatus);
//        } else if (validateStatus == 1) {
//            session.setAttribute("orderStatus", orderStatus);
//            return ResponseEntity.accepted().body(orderStatus);
//        } else {
//            session.removeAttribute("orderStatus");
//            return ResponseEntity.unprocessableContent().build();
//        }
//    }
//
//    private Integer validateOrderStatus(IOrderStatus orderStatus) {
//        boolean changed = false;
//        if (!roomService.exists(orderStatus.getRoomId())) {
//            return -1;
//        }
//        IOrder order = orderStatus.getBody();
//        if (!order.getStartTime().isBefore(order.getEndTime())) {
//            return -1;
//        }
//        if (!photographerService.exists(order.getPhotographerId())) {
//            changed = true;
//            order.setPhotographerId(null);
//        }
//        List<EquipmentDto> validEquipment = new ArrayList<>();
//        for (EquipmentDto equipment: order.getEquipment()) {
//            if (equipmentService.exists(equipment.getId())) {
//                validEquipment.add(equipment);
//            } else {
//                changed = true;
//            }
//        }
//        order.setEquipment(validEquipment);
//        if (changed) {
//            return 1;
//        }
//    }

}
