package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.repository.RoomRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Transactional
@SuppressWarnings({"PMD.LawOfDemeter"})
class RoomServiceTest extends AbstractIntegrationTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @MockitoBean
    private TimeProviderService timeProvider;

    @Value("${app.static-data.placeholder-path}")
    private String placeholderPath;

    private static final UUID ROOM_1_ID = UUID.fromString("8718f425-0ebe-48aa-9127-4541ed29524c");
    private static final UUID ROOM_2_ID = UUID.fromString("602109e2-f729-41f3-b93b-2f9a81878ed6");

    private static final LocalDateTime FROZEN_DATE_TIME = LocalDateTime.of(2026, 6, 15, 12, 0);
    private static final ZonedDateTime FROZEN_ZONED_TIME = FROZEN_DATE_TIME.atZone(ZoneId.of("Europe/Moscow"));

    @BeforeEach
    void setUpTimeProvider() {
        when(timeProvider.now()).thenReturn(FROZEN_ZONED_TIME);
    }

    @Nested
    @DisplayName("getPhotoroomsContext: Список всех фотостудий")
    class PhotoroomsContextTests {

        @Test
        @DisplayName("Должен вернуть все 3 записи комнат из БД")
        void shouldReturnAllRoomsRecords() {
            IPhotoroomsContext context = roomService.getPhotoroomsContext();

            assertSoftly(softly -> {
                softly.assertThat(context)
                        .as("Контекст фотостудий не должен быть null")
                        .isNotNull();
                softly.assertThat(context.getPhotorooms())
                        .as("В БД должно быть ровно 3 записи комнат (согласно тестовой миграции)")
                        .hasSize(3);
            });
        }

        @Test
        @DisplayName("Должен корректно мапить поля и брать первую фотографию")
        void shouldMapFieldsAndTakeFirstPhoto() {
            IPhotoroomsContext context = roomService.getPhotoroomsContext();
            List<? extends IDataCard> cards = context.getPhotorooms();

            IDataCard zal1Card = cards.stream()
                    .filter(card -> "Зал 1".equals(card.getTitle()))
                    .findFirst()
                    .orElseThrow();

            assertSoftly(softly -> {
                softly.assertThat(zal1Card.getTitle())
                        .as("Title для 'Зал 1' должен корректно замапиться")
                        .isEqualTo("Зал 1");
                softly.assertThat(zal1Card.getText())
                        .as("Text (описание) не должен быть пустым")
                        .isNotBlank();
                softly.assertThat(zal1Card.getImage())
                        .as("Должна браться первая фотография из списка photos")
                        .isEqualTo("/images/placeholder.png");
                softly.assertThat(zal1Card.getLinkUrl())
                        .as("Ссылка должна формироваться как 'photorooms/' + getOrderId")
                        .isEqualTo("photorooms/" + ROOM_1_ID);
            });
        }

        @Test
        @DisplayName("Должен использовать placeholder, если у комнаты нет фотографий")
        void shouldUsePlaceholderWhenNoPhotos() {
            IPhotoroomsContext context = roomService.getPhotoroomsContext();
            List<? extends IDataCard> cards = context.getPhotorooms();

            IDataCard zal2Card = cards.stream()
                    .filter(card -> "Зал 2".equals(card.getTitle()))
                    .findFirst()
                    .orElseThrow();

            assertEquals(placeholderPath, zal2Card.getImage(),
                    "Если у комнаты нет фото, должен использоваться placeholder из application.properties");
        }

        @Test
        @DisplayName("Должен вернуть пустой список после очистки таблицы")
        void shouldReturnEmptyListAfterCleanup() {
            roomRepository.deleteAll();
            IPhotoroomsContext context = roomService.getPhotoroomsContext();

            assertThat(context.getPhotorooms())
                    .as("Список комнат должен быть пуст после вызова deleteAll()")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("getConcretePhotoroomContext: Детальная страница студии")
    class ConcretePhotoroomContextTests {

        @Test
        @DisplayName("Должен вернуть контекст с галереей фото и префиксом 'Зал '")
        void shouldReturnConcreteContextForExistingRoom() {
            IConcretePhotoroomContext context = roomService.getConcretePhotoroomContext(ROOM_1_ID);

            assertSoftly(softly -> {
                softly.assertThat(context)
                        .as("Контекст конкретной студии не должен быть null")
                        .isNotNull();
                softly.assertThat(context.getTextData().getTitle())
                        .as("Title должен начинаться с префикса 'Зал '")
                        .startsWith("Зал ");
                softly.assertThat(context.getImages())
                        .as("Список фотографий должен содержать все фото комнаты")
                        .hasSize(3);
                softly.assertThat(context.getRoomUuid())
                        .as("ID должен совпадать с запрошенным")
                        .isEqualTo(ROOM_1_ID);
            });
        }

        @Test
        @DisplayName("Должен вернуть null для несуществующего UUID")
        void shouldReturnNullForNonExistentRoom() {
            UUID randomUuid = UUID.randomUUID();
            IConcretePhotoroomContext context = roomService.getConcretePhotoroomContext(randomUuid);

            assertNull(context,
                    "Сервис должен вернуть null, если комната с таким UUID не найдена в БД");
        }
    }

    @Nested
    @DisplayName("getRoomCalendar: Календарь доступности (7x14)")
    class RoomCalendarTests {

        private static final Integer ROOM_1_PRICE = 150_000;

        @Test
        @DisplayName("Должен вернуть null для несуществующей комнаты")
        void shouldReturnNullForNonExistentRoom() {
            UUID randomUuid = UUID.randomUUID();
            ICalendar calendar = roomService.getRoomCalendar(randomUuid, FROZEN_DATE_TIME.toLocalDate());

            assertNull(calendar, "Календарь должен быть null для несуществующей комнаты");
        }

        @Test
        @DisplayName("Должен вернуть null при запросе календаря на прошлую неделю")
        void shouldReturnNullForPastWeek() {
            LocalDate pastMonday = FROZEN_DATE_TIME.toLocalDate().with(java.time.DayOfWeek.MONDAY).minusWeeks(1);
            ICalendar calendar = roomService.getRoomCalendar(ROOM_1_ID, pastMonday);

            assertNull(calendar, "Сервис должен заблокировать просмотр календаря на прошлые недели");
        }

        @Test
        @DisplayName("Для будущей недели все слоты должны быть равны цене комнаты (нет прошедших часов)")
        void shouldFillFutureWeekWithPrice() {
            LocalDate futureMonday = FROZEN_DATE_TIME.toLocalDate().with(java.time.DayOfWeek.MONDAY).plusWeeks(1);
            ICalendar calendar = roomService.getRoomCalendar(ROOM_1_ID, futureMonday);

            assertSoftly(softly -> {
                softly.assertThat(calendar)
                        .as("Календарь для будущей недели не должен быть null")
                        .isNotNull();

                List<List<Integer>> matrix = calendar.getCalendar();

                for (int day = 0; day < 7; day++) {
                    for (int hour = 0; hour < 14; hour++) {
                        softly.assertThat(matrix.get(day).get(hour))
                                .as(String.format("Слот [%d][%d] в будущей неделе должен быть равен цене (%d)", day, hour, ROOM_1_PRICE))
                                .isEqualTo(ROOM_1_PRICE);
                    }
                }
            });
        }

        @Test
        @DisplayName("Для текущей недели прошедшие часы (до 12:00) должны быть -1")
        void shouldMarkPastHoursAsUnavailableForCurrentWeek() {
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(java.time.DayOfWeek.MONDAY);
            ICalendar calendar = roomService.getRoomCalendar(ROOM_1_ID, currentMonday);

            assertSoftly(softly -> {
                softly.assertThat(calendar)
                        .as("Календарь на текущую неделю должен успешно сгенерироваться")
                        .isNotNull();

                List<List<Integer>> matrix = calendar.getCalendar();
                int mondayIndex = 0;

                for (int hourIndex = 0; hourIndex <= 4; hourIndex++) {
                    softly.assertThat(matrix.get(mondayIndex).get(hourIndex))
                            .as("Час " + (hourIndex + 8) + ":00 уже прошел и должен быть -1")
                            .isEqualTo(-1);
                }
            });
        }

        @Test
        @DisplayName("Должен отметить реальные бронирования из БД как -1 (занято)")
        void shouldMarkRealReservationsAsBooked() {
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(java.time.DayOfWeek.MONDAY);
            ICalendar calendar = roomService.getRoomCalendar(ROOM_1_ID, currentMonday);

            assertSoftly(softly -> {
                softly.assertThat(calendar)
                        .as("Календарь должен сгенерироваться")
                        .isNotNull();

                List<List<Integer>> matrix = calendar.getCalendar();
                int mondayIndex = 0;

                softly.assertThat(matrix.get(mondayIndex).get(5))
                        .as("Слот в 13:00 должен быть занят (-1) из-за бронирования [13:00, 14:00)")
                        .isEqualTo(-1);

                for (int hourIndex = 6; hourIndex <= 13; hourIndex++) {
                    softly.assertThat(matrix.get(mondayIndex).get(hourIndex))
                            .as("Слот в " + (hourIndex + 8) + ":00 должен быть свободен (цена " + ROOM_1_PRICE + ")")
                            .isEqualTo(ROOM_1_PRICE);
                }
            });
        }

        @Test
        @DisplayName("Матрица должна иметь строго 7 дней и 14 часов")
        void shouldHaveCorrectDimensions() {
            LocalDate currentMonday = FROZEN_DATE_TIME.toLocalDate().with(java.time.DayOfWeek.MONDAY);
            ICalendar calendar = roomService.getRoomCalendar(ROOM_1_ID, currentMonday);

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

    @Nested
    @DisplayName("exists: Проверка существования комнаты")
    class ExistsTests {

        @Test
        @DisplayName("Должен вернуть true для существующей комнаты")
        void shouldReturnTrueForExistingRoom() {
            Boolean exists = roomService.exists(ROOM_1_ID);
            assertTrue(exists, "Метод exists должен вернуть true для UUID существующей комнаты");
        }

        @Test
        @DisplayName("Должен вернуть false для несуществующей комнаты")
        void shouldReturnFalseForNonExistentRoom() {
            UUID randomUuid = UUID.randomUUID();
            Boolean exists = roomService.exists(randomUuid);
            assertFalse(exists, "Метод exists должен вернуть false для случайного UUID");
        }
    }

    @Nested
    @DisplayName("getRoomById: Получение комнаты по ID")
    class GetRoomByIdTests {

        @Test
        @DisplayName("Должен вернуть доменный объект Room для существующего ID")
        void shouldReturnRoomForExistingId() {
            Room room = roomService.getRoomById(ROOM_1_ID);

            assertSoftly(softly -> {
                softly.assertThat(room)
                        .as("Комната с существующим ID не должна быть null")
                        .isNotNull();
                softly.assertThat(room.title())
                        .as("Title должен быть замаплен корректно")
                        .isEqualTo("Зал 1");
                softly.assertThat(room.price())
                        .as("Цена должна быть замаплена корректно")
                        .isEqualTo(150_000);
            });
        }

        @Test
        @DisplayName("Должен вернуть null для несуществующего ID")
        void shouldReturnNullForNonExistentId() {
            UUID randomUuid = UUID.randomUUID();
            Room room = roomService.getRoomById(randomUuid);

            assertNull(room,
                    "Метод getRoomById должен вернуть null для несуществующего UUID");
        }
    }

    @Nested
    @DisplayName("freeRoom: Проверка доступности комнаты")
    class FreeRoomTests {

        @Test
        @DisplayName("Должен вернуть false, если комната занята в указанный диапазон")
        void shouldReturnFalseIfRoomIsBusy() {
            Range<LocalDateTime> busyTime = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 9, 0),
                    LocalDateTime.of(2026, 6, 15, 10, 0)
            );

            Boolean isFree = roomService.freeRoom(ROOM_1_ID, busyTime);

            assertFalse(isFree,
                    "Комната должна быть занята (вернуть false), так как время пересекается с бронью");
        }

        @Test
        @DisplayName("Должен вернуть true, если комната свободна в указанный диапазон")
        void shouldReturnTrueIfRoomIsFree() {

            Range<LocalDateTime> freeTime = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 20, 0),
                    LocalDateTime.of(2026, 6, 15, 21, 0)
            );

            Boolean isFree = roomService.freeRoom(ROOM_1_ID, freeTime);

            assertTrue(isFree,
                    "Комната должна быть свободна (вернуть true), так как время не пересекается с бронью");
        }

        @Test
        @DisplayName("Должен вернуть false для несуществующей комнаты")
        void shouldReturnFalseForNonExistentRoom() {
            Range<LocalDateTime> time = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 9, 0),
                    LocalDateTime.of(2026, 6, 15, 10, 0)
            );

            Boolean isFree = roomService.freeRoom(UUID.randomUUID(), time);

            assertFalse(isFree,
                    "Для несуществующей комнаты метод должен вернуть false");
        }
    }
}