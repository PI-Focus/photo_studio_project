package pi.focus.server.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.ICredentials;
import pi.focus.server.api.models.IProfileOrders;
import pi.focus.server.api.models.IReservation;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.mapper.JsonMapper;
import pi.focus.server.core.security.CustomUserDetails;
import pi.focus.server.core.service.api.IOrderId;
import pi.focus.server.core.service.api.IReservationService;
import pi.focus.server.core.service.api.IUserService;
import pi.focus.server.service.models.CredentialsDto;
import pi.focus.server.service.models.OrderIdDto;
import pi.focus.server.service.models.ProfileOrdersDto;
import tools.jackson.core.JacksonException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер личного кабинета пользователя.
 * Обеспечивает управление профилем, просмотр персонального календаря заказов,
 * отмену бронирований и обновление учетных данных.
 */
@Controller
@Transactional
@RequestMapping("/profile")
@SuppressWarnings({"PMD.ConfusingTernary"})
public class ProfileController {
    private final IUserService userService;
    private final IReservationService reservationService;

    /** Конструктор контроллера с внедрением сервисов пользователя и бронирования */
    public ProfileController(IUserService userService, IReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    /**
     * Отображает страницу со списком заказов пользователя.
     * Выполняет проверку прав доступа: пользователь может видеть только свой профиль.
     * 
     * @param id строковое представление UUID пользователя
     * @param userDetails данные текущего аутентифицированного пользователя
     * @param session текущая сессия для редиректа
     * @return путь к шаблону профиля или редирект при ошибке доступа
     */
    @GetMapping("/{id}/orders")
    public String getOrders(
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
            return "pages/profile-orders";
        }
    }

    /**
     * Возвращает данные для календаря заказов пользователя на указанную дату.
     * Включает в себя информацию о занятости залов и детали конкретных бронирований.
     * 
     * @param id идентификатор пользователя
     * @param date дата для отображения (ISO формат)
     * @param userDetails данные текущего пользователя
     * @return ResponseEntity с информацией о заказах и календаре
     */
    @GetMapping("/{id}/orders/calendar")
    public ResponseEntity<IProfileOrders> getCalendar(
            @PathVariable String id,
            @RequestParam String date,
            @AuthenticationPrincipal CustomUserDetails userDetails
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

    /**
     * Обрабатывает запрос на удаление (отмену) заказа.
     * Проверяет принадлежность удаляемого заказа текущему пользователю.
     * 
     * @param id идентификатор пользователя
     * @param stringOrderId JSON с ID заказа
     * @param userDetails данные текущего пользователя
     * @return 200 OK при успехе или 400 Bad Request при ошибке
     */
    @DeleteMapping("/{id}/orders")
    public ResponseEntity<?> deleteOrder(
            @PathVariable String id,
            @RequestBody String stringOrderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        IOrderId orderId;
        UUID uuid;
        try {
            orderId = JsonMapper.getInstance().readValue(stringOrderId, OrderIdDto.class);
            uuid = UUID.fromString(id);
        } catch (JacksonException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        if (!uuid.equals(userDetails.userId())) {
            return ResponseEntity.badRequest().build();
        }
        if (orderId.getOrderId() != null && reservationService.deleteOrderById(orderId.getOrderId())) {
            return ResponseEntity.ok().build();
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Перенаправляет пользователя на страницу настроек профиля.
     * 
     * @param id идентификатор пользователя
     * @param userDetails данные текущего пользователя
     * @return путь к странице настроек (логина/регистрации в данном контексте) или редирект
     */
    @GetMapping("/{id}/options")
    public String getOptions(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
        if (!uuid.equals(userDetails.userId())) {
            return "redirect:/";
        }
        return "pages/profile-options";
    }

    /**
     * Возвращает текущие учетные данные пользователя для редактирования.
     * 
     * @param id идентификатор пользователя
     * @param userDetails данные текущего пользователя
     * @return ResponseEntity с объектом учетных данных (логин, телефон, почта)
     */
    @GetMapping("{id}/credentials")
    public ResponseEntity<ICredentials> getCredentials(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        if (!uuid.equals(userDetails.userId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(new CredentialsDto(
                        userDetails.login(),
                        null,
                        userDetails.phoneNumber(),
                        userDetails.email(),
                        null
        ));
    }

    /**
     * Обрабатывает запрос на обновление учетных данных пользователя.
     * Проверяет уникальность и валидность новых данных через сервис пользователя.
     * 
     * @param id идентификатор пользователя
     * @param stringOrderId JSON с новыми учетными данными
     * @param userDetails данные текущего пользователя
     * @return ResponseEntity с обновленными данными или ошибкой валидации
     */
    @PostMapping("{id}/credentials")
    public ResponseEntity<ICredentials> postCredentials(
            @PathVariable String id,
            @RequestBody String stringOrderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ICredentials credentials;
        UUID uuid;
        try {
            credentials = JsonMapper.getInstance().readValue(stringOrderId, CredentialsDto.class);
            uuid = UUID.fromString(id);
        } catch (JacksonException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        if (!uuid.equals(userDetails.userId())) {
            return ResponseEntity.badRequest().build();
        }
        User user = new User(
                uuid,
                credentials.getLogin(),
                credentials.getPhoneNumber(),
                credentials.getEmail(),
                credentials.getPassword(),
                UserRole.USER
        );
        String errorMes = userService.updateUser(user);
        if (errorMes == null) {
            return ResponseEntity.badRequest().build();
        } else if (errorMes.isEmpty()) {
            return ResponseEntity.ok().body(credentials);
        } else {
            credentials.setError(errorMes);
            return ResponseEntity.badRequest().body(credentials);
        }
    }

    /**
     * Вспомогательный метод для перенаправления на предыдущую посещенную страницу.
     * 
     * @param session текущая сессия пользователя
     * @return строка редиректа
     */
    private String redirectPrevious(HttpSession session) {
        String previousUri = (String) session.getAttribute("previousUri");
        if (previousUri != null) {
            session.removeAttribute("previousUri");
            return "redirect:" + previousUri;
        }
        return "redirect:/";
    }
}
