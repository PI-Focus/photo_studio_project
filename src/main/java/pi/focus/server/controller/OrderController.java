package pi.focus.server.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.mapper.JsonMapper;
import pi.focus.server.core.security.CustomUserDetails;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IOrderFacade;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.models.OrderStatusDto;
import tools.jackson.core.JacksonException;

import java.time.format.DateTimeParseException;

/**
 * Контроллер для управления процессами бронирования и оформления заказов.
 * Обеспечивает работу с календарем залов, подбор оборудования и фотографов,
 * а также управляет состоянием корзины заказа через сессию пользователя.
 */
@Controller
@RequestMapping("/order")
@Transactional
@SuppressWarnings({"PMD.AvoidDuplicateLiterals", "PMD.AvoidLiteralsInIfCondition"})
public class OrderController {
    private final IRoomService roomService;
    private final IEquipmentService equipmentService;
    private final IPhotographerService photographerService;
    private final IOrderFacade orderService;

    /** Конструктор с необходимыми сервисами для обработки логики заказов */
    public OrderController(
            IRoomService roomService,
            IEquipmentService equipmentService,
            IPhotographerService photographerService,
            IOrderFacade orderService
    ) {
        this.roomService = roomService;
        this.equipmentService = equipmentService;
        this.photographerService = photographerService;
        this.orderService = orderService;
    }

    /**
     * Возвращает календарь занятости конкретного фотозала на выбранную дату.
     * Проверяет корректность ID и даты .
     * 
     * @param id уникальный идентификатор зала
     * @param date дата для просмотра расписания 
     * @return ResponseEntity с объектом календаря или ошибка 400
     */
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
        if (localDate.isAfter(LocalDate.now().plusDays(35))) {
            return ResponseEntity.badRequest().build();
        }
        ICalendar calendar = roomService.getRoomCalendar(uuid, localDate);
        if (calendar == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(calendar);
    }

    /**
     * Предоставляет список всего доступного в студии оборудования.
     * 
     * @return список объектов оборудования
     */
    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getEquipment() {
        return ResponseEntity.ok().body(equipmentService.getEquipment());
    }

    /**
     * Возвращает список фотографов, которые свободны в указанный временной интервал.
     * 
     * @param start время начала интервала 
     * @param end время окончания интервала 
     * @return список доступных фотографов
     */
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

    /**
     * Получает текущий статус неоформленного заказа из сессии пользователя.
     * Если сессия новая, инициализирует пустой статус заказа.
     * 
     * @param request текущий HTTP запрос
     * @return текущий объект статуса заказа
     */
    @GetMapping("/current")
    public ResponseEntity<IOrderStatus> getCurrent(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        IOrderStatus orderStatus = (IOrderStatus) session.getAttribute("orderStatus");
        if (orderStatus == null) {
            orderStatus = orderService.getEmptyOrderStatus();
        }
        session.setAttribute("orderStatus", orderStatus);
        return ResponseEntity.ok().body(orderStatus);
    }

    /**
     * Обновляет данные текущего заказа в сессии после предварительной валидации.
     * 
     * @param stringOrderStatus JSON-строка с данными заказа
     * @param request текущий HTTP запрос
     * @return обновленный статус заказа или статус ошибки валидации
     */
    @PostMapping("/current")
    public ResponseEntity<IOrderStatus> postCurrent(
            @RequestBody String stringOrderStatus,
            HttpServletRequest request
    ) {
        IOrderStatus orderStatus;
        try {
            orderStatus =  JsonMapper.getInstance().readValue(stringOrderStatus, OrderStatusDto.class);
        } catch (JacksonException e) {
            return ResponseEntity.unprocessableContent().build();
        }
        HttpSession session = request.getSession(true);
        Integer validateStatus = orderService.validateOrderStatus(orderStatus);
        if (validateStatus == 0) {
            session.setAttribute("orderStatus", orderStatus);
            return ResponseEntity.ok().body(orderStatus);
        } else if (validateStatus == 1) {
            session.setAttribute("orderStatus", orderStatus);
            return ResponseEntity.accepted().body(orderStatus);
        } else {
            session.removeAttribute("orderStatus");
            return ResponseEntity.unprocessableContent().build();
        }
    }

    /**
     * Подтверждает создание бронирования и сохраняет заказ в базе данных.
     * После успешного создания очищает данные заказа в сессии.
     * 
     * @param stringOrderStatus JSON-строка с финальными данными заказа
     * @param request текущий HTTP запрос
     * @param userDetails данные авторизованного пользователя
     * @return результат операции и объект заказа
     */
    @PostMapping("/confirm")
    public ResponseEntity<IOrderStatus> createReservation (
            @RequestBody String stringOrderStatus,
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        IOrderStatus orderStatus;
        try {
            orderStatus = JsonMapper.getInstance().readValue(stringOrderStatus, OrderStatusDto.class);
        } catch (JacksonException e) {
            return ResponseEntity.unprocessableContent().build();
        }
        HttpSession session = request.getSession(false);
        Integer validateStatus = orderService.validateOrderStatus(orderStatus);
        if (validateStatus == 0) {
            session.setAttribute("orderStatus", orderStatus);
            orderService.createReservation(userDetails.userId(), orderStatus);
            session.removeAttribute("orderStatus");
            return ResponseEntity.ok().body(orderStatus);
        } else if (validateStatus == 1) {
            session.setAttribute("orderStatus", orderStatus);
            return ResponseEntity.accepted().body(orderStatus);
        } else {
            session.removeAttribute("orderStatus");
            return ResponseEntity.unprocessableContent().build();
        }
    }
}
