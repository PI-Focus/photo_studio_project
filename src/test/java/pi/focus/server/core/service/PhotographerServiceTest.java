package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.repository.PhotographerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.LongVariable"})
class PhotographerServiceTest extends AbstractIntegrationTest {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotographerRepository photographerRepository;

    private static final UUID VLADIMIR_ID = UUID.fromString("692d3820-d762-436a-93ae-aaa1c7d2c1f5");


    @Nested
    @DisplayName("getEquipmentContext: Контекст фотографов для главной страницы")
    class PhotographersContextTests {

        @Test
        @DisplayName("Должен вернуть все 3 записи фотографов из БД")
        void shouldReturnAllPhotographersRecords() {
            IPhotographersContext context = photographerService.getEquipmentContext();

            assertSoftly(softly -> {
                softly.assertThat(context)
                        .as("Контекст фотографов не должен быть null")
                        .isNotNull();
                softly.assertThat(context.getPhotographers())
                        .as("В БД должно быть ровно 3 записи фотографов (согласно тестовой миграции)")
                        .hasSize(3);
            });
        }

        @Test
        @DisplayName("Должен корректно мапить поля из Entity в DTO")
        void shouldMapAllFieldsCorrectly() {
            IPhotographersContext context = photographerService.getEquipmentContext();
            List<? extends IImagedTextCard> cards = context.getPhotographers();

            assertSoftly(softly -> {
                softly.assertThat(cards)
                        .as("Список карточек фотографов должен содержать 3 элемента")
                        .hasSize(3);

                cards.forEach(card -> {
                    softly.assertThat(card.getTitle())
                            .as("Title (Имя + Фамилия) не должен быть null или пустым")
                            .isNotNull()
                            .isNotBlank();

                    softly.assertThat(card.getText())
                            .as("Text (описание) не должен быть null")
                            .isNotNull();

                    softly.assertThat(card.getImage())
                            .as("Image (путь к фото) не должен быть null")
                            .isNotNull();
                });
            });
        }

        @Test
        @DisplayName("Должен формировать title как конкатенацию имени и фамилии")
        void shouldConcatenateNameAndSurnameInTitle() {
            IPhotographersContext context = photographerService.getEquipmentContext();
            List<? extends IImagedTextCard> cards = context.getPhotographers();

            assertSoftly(softly -> {
                List<String> titles = cards.stream()
                        .map(ITextCard::getTitle)
                        .toList();

                softly.assertThat(titles)
                        .as("Должны присутствовать все три фотографа в формате 'Имя Фамилия'")
                        .containsExactlyInAnyOrder(
                                "Владимир Кузнецов",
                                "Марина Волкова",
                                "Дмитрий Соколов"
                        );

                cards.forEach(card -> {
                    softly.assertThat(card.getTitle())
                            .as("Title должен содержать пробел между именем и фамилией")
                            .contains(" ");
                });
            });
        }

        @Test
        @DisplayName("Должен вернуть пустой список после очистки таблицы")
        void shouldReturnEmptyListAfterCleanup() {
            photographerRepository.deleteAll();
            IPhotographersContext context = photographerService.getEquipmentContext();

            assertThat(context.getPhotographers())
                    .as("Список фотографов должен быть пуст после вызова deleteAll()")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("getPhotographersByTime: Получение свободных фотографов")
    class GetPhotographersByTimeTests {

        @Test
        @DisplayName("Должен вернуть только свободных фотографов для заданного диапазона времени")
        void shouldReturnOnlyFreePhotographersForGivenTimeRange() {
            Range<LocalDateTime> busyTimeForSome = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 10, 0),
                    LocalDateTime.of(2026, 6, 15, 11, 0)
            );

            List<Photographer> freePhotographers = photographerService.getPhotographersByTime(busyTimeForSome);

            assertSoftly(softly -> {
                softly.assertThat(freePhotographers)
                        .as("Должен быть возвращен только 1 свободный фотограф (Марина)")
                        .hasSize(1);
                softly.assertThat(freePhotographers.getFirst().name() + " " + freePhotographers.getFirst().surname())
                        .as("Свободным фотографом должна быть Марина Волкова")
                        .isEqualTo("Марина Волкова");
            });
        }

        @Test
        @DisplayName("Должен вернуть всех фотографов, если диапазон времени полностью свободен")
        void shouldReturnAllPhotographersWhenTimeRangeIsFree() {
            // Вечер 15 июня все свободны (последняя бронь до 19:00)
            Range<LocalDateTime> freeTime = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 20, 0),
                    LocalDateTime.of(2026, 6, 15, 21, 0)
            );

            List<Photographer> freePhotographers = photographerService.getPhotographersByTime(freeTime);

            assertThat(freePhotographers)
                    .as("Все 3 фотографа должны быть свободны в это время")
                    .hasSize(3);
        }
    }

    @Nested
    @DisplayName("exists: Проверка существования фотографа")
    class ExistsTests {

        @Test
        @DisplayName("Должен вернуть true для существующего фотографа")
        void shouldReturnTrueForExistingPhotographer() {
            Boolean exists = photographerService.exists(VLADIMIR_ID);

            assertTrue(exists,
                    "Метод exists должен вернуть true для UUID существующего фотографа");
        }

        @Test
        @DisplayName("Должен вернуть false для несуществующего фотографа")
        void shouldReturnFalseForNonExistentPhotographer() {
            UUID randomUuid = UUID.randomUUID();
            Boolean exists = photographerService.exists(randomUuid);

            assertFalse(exists,
                    "Метод exists должен вернуть false для случайного UUID");
        }
    }

    @Nested
    @DisplayName("getPhotographerById: Получение фотографа по ID")
    class GetPhotographerByIdTests {

        @Test
        @DisplayName("Должен вернуть доменный объект Photographer для существующего ID")
        void shouldReturnPhotographerForExistingId() {
            Photographer photographer = photographerService.getPhotographerById(VLADIMIR_ID);

            assertSoftly(softly -> {
                softly.assertThat(photographer)
                        .as("Фотограф с существующим ID не должен быть null")
                        .isNotNull();
                softly.assertThat(photographer.name())
                        .as("Имя должно быть замаплено корректно")
                        .isEqualTo("Владимир");
                softly.assertThat(photographer.surname())
                        .as("Фамилия должна быть замаплена корректно")
                        .isEqualTo("Кузнецов");
            });
        }

        @Test
        @DisplayName("Должен вернуть null для несуществующего ID")
        void shouldReturnNullForNonExistentId() {
            UUID randomUuid = UUID.randomUUID();
            Photographer photographer = photographerService.getPhotographerById(randomUuid);

            assertNull(photographer,
                    "Метод getPhotographerById должен вернуть null для несуществующего UUID");
        }
    }

    @Nested
    @DisplayName("freePhotographer: Проверка доступности фотографа")
    class FreePhotographerTests {

        @Test
        @DisplayName("Должен вернуть false, если фотограф занят в указанный диапазон")
        void shouldReturnFalseIfPhotographerIsBusy() {
            Range<LocalDateTime> busyTime = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 10, 0),
                    LocalDateTime.of(2026, 6, 15, 11, 0)
            );

            boolean isFree = photographerService.freePhotographer(VLADIMIR_ID, busyTime);

            assertFalse(isFree,
                    "Фотограф должен быть занят (вернуть false), так как время пересекается с бронью");
        }

        @Test
        @DisplayName("Должен вернуть true, если фотограф свободен в указанный диапазон")
        void shouldReturnTrueIfPhotographerIsFree() {
            Range<LocalDateTime> freeTime = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 20, 0),
                    LocalDateTime.of(2026, 6, 15, 21, 0)
            );

            boolean isFree = photographerService.freePhotographer(VLADIMIR_ID, freeTime);

            assertTrue(isFree,
                    "Фотограф должен быть свободен (вернуть true), так как время не пересекается с бронью");
        }

        @Test
        @DisplayName("Должен вернуть false для несуществующего фотографа")
        void shouldReturnFalseForNonExistentPhotographer() {
            Range<LocalDateTime> time = Range.closed(
                    LocalDateTime.of(2026, 6, 15, 10, 0),
                    LocalDateTime.of(2026, 6, 15, 11, 0)
            );

            boolean isFree = photographerService.freePhotographer(UUID.randomUUID(), time);

            assertFalse(isFree,
                    "Для несуществующего фотографа метод должен вернуть false");
        }
    }
}