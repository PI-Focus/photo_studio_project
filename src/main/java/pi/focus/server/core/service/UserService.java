package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.entity.ReservationEntity;
import pi.focus.server.core.entity.ReservedEquipmentEntity;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.service.api.IUserService;
import pi.focus.server.service.models.CalendarDto;
import pi.focus.server.service.models.ReservationDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile({"dev", "prod", "test"})
public class UserService implements IUserService {
    private static final int MIN_LOGIN_LENGTH = 4;
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Value("${app.static-data.placeholder-path}")
    private String placeholderPath;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReservationService reservationService;
    private final TimeProviderService timeProvider;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TimeProviderService timeProvider, ReservationService reservationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.timeProvider = timeProvider;
        this.reservationService = reservationService;
    }

    @Override
    public boolean createUser(User user) {
        if (existsByLogin(user.login())) {
            return false;
        }
        userRepository.save(
                new UserEntity(
                        user.id(),
                        user.login(),
                        user.phoneNumber(),
                        user.email(),
                        passwordEncoder.encode(user.password()),
                        user.role(),
                        new ArrayList<>()
                )
        );
        return true;
    }

    @Override
    public List<ReservationDto> getUserReservationDtos(UUID id, LocalDate day) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return new ArrayList<>();
        }
        UserEntity user = userOpt.get();
        List<ReservationDto> list = new ArrayList<>();

        ZonedDateTime zonedNow = timeProvider.now();
        LocalDateTime localZonedNow = zonedNow.toLocalDateTime();
        LocalDate nowDay = zonedNow.toLocalDate();

        List<List<Integer>> calendar = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.add(new ArrayList<>(Collections.nCopies(14, -1)));
        }
        LocalDate monday = day.with(DayOfWeek.MONDAY);
        boolean isNextWeek = false;
        if (monday.isBefore(nowDay.with(DayOfWeek.MONDAY))) {
            return new ArrayList<>();
        } else if (monday.isAfter(nowDay.with(DayOfWeek.MONDAY))) {
            isNextWeek = true;
        }

        for (ReservationEntity reservation: user.getReservations()) {
            Range<LocalDateTime> range = reservation.getTime();
            LocalDateTime start = range.lower();
            if (isNextWeek || localZonedNow.isBefore(start)) {
                list.add(new ReservationDto(
                        reservation.getId(),
                        reservation.getRoom().getTitle(),
                        range.lower(),
                        range.upper()
                ));
            }
        }
        return list;
    }

    @Override
    public ICalendar getUserCalendar(UUID id, LocalDate day) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return null;
        }
        UserEntity user = userOpt.get();

        ZonedDateTime zonedNow = timeProvider.now();
        LocalDateTime localZonedNow = zonedNow.toLocalDateTime();
        LocalDate nowDay = zonedNow.toLocalDate();

        List<List<Integer>> calendar = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.add(new ArrayList<>(Collections.nCopies(14, -1)));
        }
        LocalDate monday = day.with(DayOfWeek.MONDAY);
        boolean isNextWeek = false;
        if (monday.isBefore(nowDay.with(DayOfWeek.MONDAY))) {
            return null;
        } else if (monday.isAfter(nowDay.with(DayOfWeek.MONDAY))) {
            isNextWeek = true;
        }

        for (ReservationEntity reservation: user.getReservations()) {
            Range<LocalDateTime> range = reservation.getTime();
            LocalDateTime start = range.lower();
            if (isNextWeek || localZonedNow.isBefore(start)) {
                reservationToCalendar(calendar, reservation, monday);
            }
        }
        return new CalendarDto(calendar);
    }

    @Override
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public String updateUser(User user) {
        Optional<UserEntity> userOpt = userRepository.findById(user.id());
        if (userOpt.isEmpty()) {
            return null;
        }
        UserEntity userEntity = userOpt.get();

        if (user.login() != null && !user.login().matches("^[a-z0-9_-]+$")) {
            return "Логин может содержать только строчные латинские буквы, цифры, символы нижнего подчеркивания и дефиса";
        }
        if (user.login() != null && !user.login().matches(".*[a-z].*")) {
            return "Логин должен содержать латинские буквы";
        }
        if (user.login() != null && user.login().length() < MIN_LOGIN_LENGTH) {
            return "Минимальная длина логина должна быть 4 символов";
        }
        if (user.login() != null && !user.login().equals(userEntity.getLogin())
                && userRepository.existsByLogin(user.login())) {
            return "Логин занят";
        }
        if (user.password() != null && user.password().length() < MIN_PASSWORD_LENGTH) {
            return "Минимальная длина пароля должна быть 8 символов";
        }
        if (user.phoneNumber() != null && !user.phoneNumber().equals(userEntity.getPhoneNumber())
                && userRepository.existsByPhoneNumber(user.phoneNumber())) {
            return "Пользователь с таким номером телефона существует";
        }
        if (user.phoneNumber() != null && !user.phoneNumber().matches("^8\\d{10}$")) {
            return "Некорректный номер телефона";
        }
        if (user.email() != null && !user.email().equals(userEntity.getEmail())
                && userRepository.existsByEmail(user.email())) {
            return "Пользователь с таким email телефона существует";
        }
        if (user.email() != null && !user.email().matches("^[^@]+@[^@]+$")) {
            return "Некорректный email";
        }

        if (user.login() != null) {
            userEntity.setLogin(user.login());
        }
        if (user.password() != null) {
            userEntity.setPassword(passwordEncoder.encode(user.password()));
        }
        if (user.phoneNumber() != null) {
            userEntity.setPhoneNumber(user.phoneNumber());
        }
        if (user.email() != null) {
            userEntity.setEmail(user.email());
        }
        userRepository.save(userEntity);
        return "";
    }

    private boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    private void reservationToCalendar(
            List<List<Integer>> calendar,
            ReservationEntity reservation,
            LocalDate monday
    ) {
        Range<LocalDateTime> range = reservation.getTime();
        LocalDateTime start = range.lower();
        LocalDateTime end = range.upper();
        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            LocalDate currentDay = monday.plusDays(dayIndex);
            if (start.toLocalDate().equals(currentDay)) {
                int startHour = start.getHour();
                int endHour = end.getHour();
                for (int hour = 8; hour < 22; hour++) {
                    if (startHour <= hour && hour < endHour) {
                        calendar.get(dayIndex).set(hour - 8, reservationPrice(reservation));
                    }
                }
            }
        }
    }

    private Integer reservationPrice(ReservationEntity reservation) {
        int price = 0;
        price += reservation.getRoom().getPrice();
        price += reservation.getPhotographer().getPrice();
        for (ReservedEquipmentEntity reservedEquipmentEntity : reservation.getReservedEquipments()) {
            price += reservedEquipmentEntity.getCount() * reservedEquipmentEntity.getEquipment().getPrice();
        }
        return price;
    }
}
