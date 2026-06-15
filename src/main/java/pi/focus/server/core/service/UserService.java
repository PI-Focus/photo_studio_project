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
