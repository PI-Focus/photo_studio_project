package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.repository.EquipmentRepository;
import pi.focus.server.core.service.api.IOrderFacade;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SuppressWarnings("PMD.LawOfDemeter")
class EquipmentServiceTest extends AbstractIntegrationTest {

    @Autowired
    private EquipmentService eqService;

    @Autowired
    private EquipmentRepository eqRepository;

    @MockitoBean
    private IOrderFacade orderFacade;

    private static final UUID EQUIPMENT_1_ID = UUID.fromString("9596154e-ee45-454a-adda-084fca722807");

    @Nested
    @DisplayName("getEquipmentContext: Контекст оборудования для главной страницы")
    class EquipmentContextTests {

        @Test
        @DisplayName("Должен вернуть все 3 записи оборудования из БД")
        void shouldReturnAllEquipmentRecords() {
            IEquipmentContext context = eqService.getEquipmentContext();

            assertSoftly(softly -> {
                softly.assertThat(context)
                        .as("Контекст оборудования не должен быть null")
                        .isNotNull();
                softly.assertThat(context.getEquipment())
                        .as("В БД должно быть ровно 3 записи оборудования (согласно тестовой миграции)")
                        .hasSize(3);
            });
        }

        @Test
        @DisplayName("Должен корректно мапить поля title, description и photoPath из Entity в DTO")
        void shouldMapFieldsCorrectly() {
            IEquipmentContext context = eqService.getEquipmentContext();
            List<? extends IImagedTextCard> cards = context.getEquipment();

            assertSoftly(softly -> {
                softly.assertThat(cards)
                        .as("Список карточек оборудования должен содержать 3 элемента")
                        .hasSize(3);

                cards.forEach(card -> {
                    softly.assertThat(card.getTitle())
                            .as("Title не должен быть null")
                            .isNotNull();
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
        @DisplayName("Должен вернуть пустой список после очистки таблицы")
        void shouldReturnEmptyListAfterCleanup() {
            eqRepository.deleteAll();
            IEquipmentContext context = eqService.getEquipmentContext();

            assertThat(context.getEquipment())
                    .as("Список оборудования должен быть пуст после вызова deleteAll()")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("getEquipment: Список доменных объектов Equipment")
    class GetEquipmentTests {

        @Test
        @DisplayName("Должен вернуть все 3 записи оборудования как доменные объекты")
        void shouldReturnAllEquipmentAsDomainObjects() {
            List<Equipment> equipment = eqService.getEquipment();

            assertSoftly(softly -> {
                softly.assertThat(equipment)
                        .as("Должно быть возвращено 3 объекта Equipment")
                        .hasSize(3);

                equipment.forEach(eq -> {
                    softly.assertThat(eq)
                            .as("Объект Equipment не должен быть null")
                            .isNotNull();
                });
            });
        }

        @Test
        @DisplayName("Должен вернуть пустой список после очистки таблицы")
        void shouldReturnEmptyListAfterCleanup() {
            eqRepository.deleteAll();
            List<Equipment> equipment = eqService.getEquipment();

            assertThat(equipment)
                    .as("Список Equipment должен быть пуст после вызова deleteAll()")
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("exists: Проверка существования оборудования")
    class ExistsTests {

        @Test
        @DisplayName("Должен вернуть true для существующего оборудования")
        void shouldReturnTrueForExistingEquipment() {
            Boolean exists = eqService.exists(EQUIPMENT_1_ID);

            assertTrue(exists,
                    "Метод exists должен вернуть true для UUID существующего оборудования");
        }

        @Test
        @DisplayName("Должен вернуть false для несуществующего оборудования")
        void shouldReturnFalseForNonExistentEquipment() {
            UUID randomUuid = UUID.randomUUID();
            Boolean exists = eqService.exists(randomUuid);

            assertFalse(exists,
                    "Метод exists должен вернуть false для случайного UUID");
        }
    }

    @Nested
    @DisplayName("getEquipmentById: Получение оборудования по ID")
    class GetEquipmentByIdTests {

        @Test
        @DisplayName("Должен вернуть доменный объект Equipment для существующего ID")
        void shouldReturnEquipmentForExistingId() {
            Equipment equipment = eqService.getEquipmentById(EQUIPMENT_1_ID);

            assertSoftly(softly -> {
                softly.assertThat(equipment)
                        .as("Оборудование с существующим ID не должно быть null")
                        .isNotNull();
                softly.assertThat(equipment.title())
                        .as("Title должен быть замаплен корректно")
                        .isNotBlank();
                softly.assertThat(equipment.description())
                        .as("Description должен быть замаплен корректно")
                        .isNotBlank();
                softly.assertThat(equipment.photoPath())
                        .as("PhotoPath должен быть замаплен корректно")
                        .isNotBlank();
            });
        }

        @Test
        @DisplayName("Должен вернуть null для несуществующего ID")
        void shouldReturnNullForNonExistentId() {
            UUID randomUuid = UUID.randomUUID();
            Equipment equipment = eqService.getEquipmentById(randomUuid);

            assertNull(equipment,
                    "Метод getEquipmentById должен вернуть null для несуществующего UUID");
        }
    }
}