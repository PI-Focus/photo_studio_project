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
import pi.focus.server.core.domain.User;
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
@SuppressWarnings({"PMD.LongVariable", "PMD.TooManyMethods"})
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
    private static final UUID USER_1_ID = UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd");
    private static final UUID USER_2_ID = UUID.fromString("e4a507da-7b8b-4f4b-9957-9d592d474621");

    private static final LocalDateTime FROZEN_DATE_TIME = LocalDateTime.of(2026, 6, 15, 12, 0);
    private static final ZonedDateTime FROZEN_ZONED_TIME = FROZEN_DATE_TIME.atZone(ZoneId.of("Europe/Moscow"));

    @BeforeEach
    void setUpTimeProvider() {
        when(timeProvider.now()).thenReturn(FROZEN_ZONED_TIME);
    }

    @Nested
    @DisplayName("createUser: Создание пользователя")
    class CreateUserTests {

        @Test
        @DisplayName("Должен успешно создать пользователя и сохранить все поля в БД")
        void shouldCreateUserSuccessfully() {
            String login = "new_user_test";
            User newUser = new User(null, login, PHONENUMBER, EMAIL, PASSWORD, UserRole.USER);

            boolean result = userService.createUser(newUser);

            UserEntity savedUser = userRepository.findByLogin(login).orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("Метод должен вернуть true при успешном создании")
                        .isTrue();
                softly.assertThat(savedUser.getId())
                        .as("ID должен быть сгенерирован автоматически")
                        .isNotNull();
                softly.assertThat(savedUser.getLogin())
                        .as("Логин должен совпадать")
                        .isEqualTo(login);
                softly.assertThat(savedUser.getPhoneNumber())
                        .as("Номер телефона должен совпадать")
                        .isEqualTo(PHONENUMBER);
                softly.assertThat(savedUser.getEmail())
                        .as("Email должен совпадать")
                        .isEqualTo(EMAIL);
                softly.assertThat(savedUser.getRole())
                        .as("Роль должна совпадать")
                        .isEqualTo(UserRole.USER);
                softly.assertThat(savedUser.getPassword())
                        .as("Пароль должен быть захеширован")
                        .isNotEqualTo(PASSWORD);
            });
        }

        @Test
        @DisplayName("Должен вернуть false, если логин уже существует в БД")
        void shouldReturnFalseIfLoginAlreadyExists() {
            String login = "duplicate_login_test";
            User user1 = new User(null, login, PHONENUMBER, EMAIL, PASSWORD, UserRole.USER);

            userService.createUser(user1);
            boolean result = userService.createUser(user1);

            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("Должен вернуть false при дубликате логина")
                        .isFalse();

                UserEntity savedUser = userRepository.findByLogin(login).orElseThrow();
                softly.assertThat(savedUser.getPhoneNumber())
                        .as("Должен сохраниться номер первого пользователя")
                        .isEqualTo(PHONENUMBER);
            });
        }

        @Test
        @DisplayName("Должен корректно сохранить роль ADMIN")
        void shouldSaveAdminRoleCorrectly() {
            String login = "admin_test_user";
            User adminUser = new User(null, login, PHONENUMBER, EMAIL, PASSWORD, UserRole.ADMIN);

            userService.createUser(adminUser);

            UserEntity savedUser = userRepository.findByLogin(login).orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(savedUser.getRole())
                        .as("Роль ADMIN должна быть сохранена корректно")
                        .isEqualTo(UserRole.ADMIN);
            });
        }

        @Test
        @DisplayName("Должен сохранить пароль в хешированном виде, а не в открытом")
        void shouldSaveHashedPasswordInsteadOfPlainText() {
            String login = "hash_test_user";
            User newUser = new User(null, login, PHONENUMBER, EMAIL, PASSWORD, UserRole.USER);

            userService.createUser(newUser);

            UserEntity savedUser = userRepository.findByLogin(login).orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(savedUser.getPassword())
                        .as("Пароль в базе данных не должен быть в открытом виде")
                        .isNotEqualTo(PASSWORD);
                softly.assertThat(savedUser.getPassword())
                        .as("Пароль должен быть BCrypt хешем")
                        .startsWith("$2a$");
            });
        }

        @Test
        @DisplayName("Должен создать пользователя с null email и phoneNumber")
        void shouldCreateUserWithNullOptionalFields() {
            String login = "optional_null_user";
            User newUser = new User(null, login, null, null, PASSWORD, UserRole.USER);

            boolean result = userService.createUser(newUser);

            UserEntity savedUser = userRepository.findByLogin(login).orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(result)
                        .as("Метод должен вернуть true при создании с null полями")
                        .isTrue();
                softly.assertThat(savedUser.getPhoneNumber())
                        .as("Номер телефона должен быть null")
                        .isNull();
                softly.assertThat(savedUser.getEmail())
                        .as("Email должен быть null")
                        .isNull();
            });
        }
    }

    @Nested
    @DisplayName("updateUser: Обновление данных пользователя")
    class UpdateUserTests {

        @Test
        @DisplayName("Должен вернуть null для несуществующего пользователя")
        void shouldReturnNullForNonExistentUser() {
            UUID randomUuid = UUID.randomUUID();
            User user = new User(randomUuid, "newlogin", null, null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertNull(error, "Для несуществующего пользователя должен вернуться null");
        }

        @Test
        @DisplayName("Должен успешно обновить все поля и вернуть пустую строку")
        void shouldUpdateAllFieldsSuccessfully() {
            User user = new User(USER_1_ID, "updatedlogin", "89999999999", "newemail@e.com", "newpassword123", UserRole.USER);

            String error = userService.updateUser(user);

            UserEntity updatedUser = userRepository.findById(USER_1_ID).orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(error)
                        .as("При успешном обновлении должна вернуться пустая строка")
                        .isEmpty();
                softly.assertThat(updatedUser.getLogin())
                        .as("Логин должен быть обновлен")
                        .isEqualTo("updatedlogin");
                softly.assertThat(updatedUser.getPhoneNumber())
                        .as("Телефон должен быть обновлен")
                        .isEqualTo("89999999999");
                softly.assertThat(updatedUser.getEmail())
                        .as("Email должен быть обновлен")
                        .isEqualTo("newemail@e.com");
                softly.assertThat(updatedUser.getPassword())
                        .as("Пароль должен быть захеширован")
                        .isNotEqualTo("newpassword123");
            });
        }

        @Test
        @DisplayName("Должен обновить только указанные поля, оставив остальные без изменений")
        void shouldUpdateOnlySpecifiedFields() {
            UserEntity originalUser = userRepository.findById(USER_1_ID).orElseThrow();
            String originalPhone = originalUser.getPhoneNumber();

            User user = new User(USER_1_ID, null, null, "onlyemail@e.com", null, UserRole.USER);

            String error = userService.updateUser(user);

            UserEntity updatedUser = userRepository.findById(USER_1_ID).orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(error)
                        .as("При успешном обновлении должна вернуться пустая строка")
                        .isEmpty();
                softly.assertThat(updatedUser.getEmail())
                        .as("Email должен быть обновлен")
                        .isEqualTo("onlyemail@e.com");
                softly.assertThat(updatedUser.getPhoneNumber())
                        .as("Телефон не должен измениться")
                        .isEqualTo(originalPhone);
            });
        }

        @Test
        @DisplayName("Должен вернуть ошибку при недопустимых символах в логине")
        void shouldReturnErrorForInvalidLoginCharacters() {
            User user = new User(USER_1_ID, "InvalidLogin!", null, null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о недопустимых символах")
                    .contains("строчные латинские буквы"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку, если логин не содержит латинских букв")
        void shouldReturnErrorForLoginWithoutLetters() {
            User user = new User(USER_1_ID, "12345", null, null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка об отсутствии латинских букв")
                    .contains("латинские буквы"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку при слишком коротком логине")
        void shouldReturnErrorForTooShortLogin() {
            User user = new User(USER_1_ID, "abc", null, null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о минимальной длине")
                    .contains("4"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку, если логин уже занят")
        void shouldReturnErrorForOccupiedLogin() {

            User user = new User(USER_2_ID, "login1", null, null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о занятом логине")
                    .contains("Логин занят"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку при слишком коротком пароле")
        void shouldReturnErrorForTooShortPassword() {
            User user = new User(USER_1_ID, null, null, null, "short", UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о минимальной длине пароля")
                    .contains("8"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку при некорректном номере телефона")
        void shouldReturnErrorForInvalidPhoneNumber() {
            User user = new User(USER_1_ID, null, "invalid-phone", null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о некорректном номере")
                    .contains("Некорректный номер телефона"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку, если номер телефона уже занят")
        void shouldReturnErrorForOccupiedPhoneNumber() {

            User user = new User(USER_2_ID, null, "82222222222", null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о занятом номере")
                    .contains("номером телефона"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку при некорректном email")
        void shouldReturnErrorForInvalidEmail() {
            User user = new User(USER_1_ID, null, null, "invalid-email", null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о некорректном email")
                    .contains("Некорректный email"));
        }

        @Test
        @DisplayName("Должен вернуть ошибку, если email уже занят")
        void shouldReturnErrorForOccupiedEmail() {
            User user = new User(USER_2_ID, null, null, "email2@e", null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("Должна вернуться ошибка о занятом email")
                    .contains("email"));
        }

        @Test
        @DisplayName("Должен позволить сохранить текущий логин без ошибки о занятости")
        void shouldAllowSameLogin() {
            UserEntity originalUser = userRepository.findById(USER_1_ID).orElseThrow();
            String currentLogin = originalUser.getLogin();

            User user = new User(USER_1_ID, currentLogin, null, null, null, UserRole.USER);

            String error = userService.updateUser(user);

            assertSoftly(softly -> softly.assertThat(error)
                    .as("При сохранении текущего логина не должно быть ошибок")
                    .isEmpty());
        }
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
            LocalDate pastMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY).minusWeeks(1);

            List<ReservationDto> reservations = userService.getUserReservationDtos(USER_1_ID, pastMonday);

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
            LocalDate pastMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY).minusWeeks(1);

            ICalendar calendar = userService.getUserCalendar(USER_1_ID, pastMonday);

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


            List<List<Integer>> matrix = calendar.getCalendar();

            assertSoftly(softly -> {
                assertNotNull(calendar, "Календарь не должен быть null");
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


            List<List<Integer>> matrix = calendar.getCalendar();

            assertSoftly(softly -> {
                assertNotNull(calendar, "Календарь не должен быть null");
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
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(DayOfWeek.MONDAY);

            ICalendar calendar = userService.getUserCalendar(USER_1_ID, currentMonday);



            assertSoftly(softly -> {
                assertNotNull(calendar, "Календарь не должен быть null");
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