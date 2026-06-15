package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.entity.PhotographerEntity;
import pi.focus.server.core.entity.ReservationEntity;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.EquipmentRepository;
import pi.focus.server.core.repository.PhotographerRepository;
import pi.focus.server.core.repository.ReservationRepository;
import pi.focus.server.core.repository.RoomRepository;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.service.models.ReservationDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@Transactional
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.LongVariable", "PMD.AvoidDuplicateLiterals"})
class UserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;


    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private TimeProviderService timeProvider;

    private static final String PASSWORD = "test_password";
    private static final String PHONENUMBER = "79991234567";
    private static final String EMAIL = "test@example.com";

    private static final UUID ROOM_1_ID = UUID.fromString("8718f425-0ebe-48aa-9127-4541ed29524c");
    private static final UUID PHOTOGRAPHER_1_ID = UUID.fromString("692d3820-d762-436a-93ae-aaa1c7d2c1f5");
    private static final UUID EQUIPMENT_1_ID = UUID.fromString("9596154e-ee45-454a-adda-084fca722807");

    private static final LocalDateTime FROZEN_DATE_TIME = LocalDateTime.of(2026, 6, 15, 12, 0);
    private static final ZonedDateTime FROZEN_ZONED_TIME = FROZEN_DATE_TIME.atZone(ZoneId.of("Europe/Moscow"));

    @BeforeEach
    void setUpTimeProvider() {
        when(timeProvider.now()).thenReturn(FROZEN_ZONED_TIME);
    }


    @Nested
    @DisplayName("getUserReservationDtos: Получение списка бронирований пользователя")
    class GetUserReservationDtosTests {

        @Test
        @DisplayName("Должен вернуть пустой список для несуществующего пользователя")
        void shouldReturnEmptyListForNonExistentUser() {
            UUID randomUuid = UUID.randomUUID();
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY);

            List<ReservationDto> reservations = userService.getUserReservationDtos(randomUuid, currentMonday);

            assertThat(reservations)
                    .as("Для несуществующего пользователя должен вернуться пустой список")
                    .isEmpty();
        }

        @Test
        @DisplayName("Должен вернуть пустой список для прошлой недели")
        void shouldReturnEmptyListForPastWeek() {
            UUID userId = UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd");
            LocalDate pastMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY).minusWeeks(1);

            List<ReservationDto> reservations = userService.getUserReservationDtos(userId, pastMonday);

            assertThat(reservations)
                    .as("Для прошлой недели должен вернуться пустой список")
                    .isEmpty();
        }

        @Test
        @DisplayName("Должен вернуть только будущие бронирования для текущей недели")
        void shouldReturnOnlyFutureReservationsForCurrentWeek() {
            UserEntity testUser = createTestUserWithReservation(
                    LocalDateTime.of(2026, 6, 15, 14, 0),
                    LocalDateTime.of(2026, 6, 15, 16, 0)
            );

            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY);

            List<ReservationDto> reservations = userService.getUserReservationDtos(
                    testUser.getId(), currentMonday
            );

            assertSoftly(softly -> {
                softly.assertThat(reservations)
                        .as("Должно быть возвращено 1 бронирование")
                        .hasSize(1);
                softly.assertThat(reservations.getFirst().start())
                        .as("Время начала должно совпадать")
                        .isEqualTo(LocalDateTime.of(2026, 6, 15, 14, 0));
                softly.assertThat(reservations.getFirst().end())
                        .as("Время окончания должно совпадать")
                        .isEqualTo(LocalDateTime.of(2026, 6, 15, 16, 0));
            });
        }

        @Test
        @DisplayName("Должен вернуть все бронирования для будущей недели")
        void shouldReturnAllReservationsForFutureWeek() {
            UserEntity testUser = createTestUserWithReservation(
                    LocalDateTime.of(2026, 6, 22, 10, 0),
                    LocalDateTime.of(2026, 6, 22, 12, 0)
            );

            LocalDate futureMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY).plusWeeks(1);

            List<ReservationDto> reservations = userService.getUserReservationDtos(
                    testUser.getId(), futureMonday
            );

            assertThat(reservations)
                    .as("Для будущей недели должны вернуться все бронирования")
                    .hasSize(1);
        }
    }

    @Nested
    @DisplayName("getUserCalendar: Получение календаря бронирований пользователя")
    class GetUserCalendarTests {

        @Test
        @DisplayName("Должен вернуть null для несуществующего пользователя")
        void shouldReturnNullForNonExistentUser() {
            UUID randomUuid = UUID.randomUUID();
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY);

            ICalendar calendar = userService.getUserCalendar(randomUuid, currentMonday);

            assertNull(calendar, "Для несуществующего пользователя должен вернуться null");
        }

        @Test
        @DisplayName("Должен вернуть null для прошлой недели")
        void shouldReturnNullForPastWeek() {
            UUID userId = UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd");
            LocalDate pastMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY).minusWeeks(1);

            ICalendar calendar = userService.getUserCalendar(userId, pastMonday);

            assertNull(calendar, "Для прошлой недели должен вернуться null");
        }

        @Test
        @DisplayName("Должен корректно заполнить календарь для текущей недели")
        void shouldFillCalendarCorrectlyForCurrentWeek() {
            UserEntity testUser = createTestUserWithReservation(
                    LocalDateTime.of(2026, 6, 15, 14, 0),
                    LocalDateTime.of(2026, 6, 15, 16, 0)
            );

            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY);

            ICalendar calendar = userService.getUserCalendar(testUser.getId(), currentMonday);

            assertNotNull(calendar, "Календарь не должен быть null");
            List<List<Integer>> matrix = calendar.getCalendar();

            assertSoftly(softly -> {
                softly.assertThat(matrix.getFirst().get(6))
                        .as("Слот в 14:00 должен быть заполнен ценой бронирования")
                        .isGreaterThan(0);
                softly.assertThat(matrix.getFirst().get(7))
                        .as("Слот в 15:00 должен быть заполнен ценой бронирования")
                        .isGreaterThan(0);
                softly.assertThat(matrix.getFirst().get(5))
                        .as("Слот в 13:00 должен быть -1 (пустой)")
                        .isEqualTo(-1);
            });
        }

        @Test
        @DisplayName("Должен корректно заполнить календарь для будущей недели")
        void shouldFillCalendarCorrectlyForFutureWeek() {
            UserEntity testUser = createTestUserWithReservation(
                    LocalDateTime.of(2026, 6, 22, 10, 0),
                    LocalDateTime.of(2026, 6, 22, 12, 0)
            );

            LocalDate futureMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY).plusWeeks(1);

            ICalendar calendar = userService.getUserCalendar(testUser.getId(), futureMonday);

            assertNotNull(calendar, "Календарь не должен быть null");
            List<List<Integer>> matrix = calendar.getCalendar();

            assertSoftly(softly -> {
                softly.assertThat(matrix.getFirst().get(2))
                        .as("Слот в 10:00 должен быть заполнен ценой бронирования")
                        .isGreaterThan(0);
                softly.assertThat(matrix.getFirst().get(3))
                        .as("Слот в 11:00 должен быть заполнен ценой бронирования")
                        .isGreaterThan(0);
            });
        }

        @Test
        @DisplayName("Матрица календаря должна иметь правильные размеры")
        void shouldHaveCorrectDimensions() {
            UUID userId = UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd");
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY);

            ICalendar calendar = userService.getUserCalendar(userId, currentMonday);

            assertNotNull(calendar, "Календарь не должен быть null");

            assertSoftly(softly -> {
                softly.assertThat(calendar.getROWS())
                        .as("Календарь должен иметь 14 строк (часов с 8 до 22)")
                        .isEqualTo(14);
                softly.assertThat(calendar.getCOLUMNS())
                        .as("Календарь должен иметь 7 столбцов (дней недели)")
                        .isEqualTo(7);
            });
        }
    }

    private UserEntity createTestUserWithReservation(LocalDateTime start, LocalDateTime end) {
        UserEntity user = new UserEntity(
                null,
                "test_user_cal_" + UUID.randomUUID().toString().substring(0, 8),
                PHONENUMBER,
                EMAIL,
                "hashed_password",
                UserRole.USER,
                new ArrayList<>()
        );
        user = userRepository.save(user);

        RoomEntity room = roomRepository.findById(ROOM_1_ID).orElseThrow();
        PhotographerEntity photographer = photographerRepository.findById(PHOTOGRAPHER_1_ID).orElseThrow();

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setPhotographer(photographer);
        reservation.setTime(Range.closed(start, end));
        reservation.setReservedEquipments(new ArrayList<>());


        reservationRepository.saveAndFlush(reservation);


        entityManager.clear();


        return userRepository.findById(user.getId()).orElseThrow();
    }
}