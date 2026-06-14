package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.core.repository.EquipmentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SuppressWarnings("PMD.LawOfDemeter")
class EquipmentServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private EquipmentService eqService;

    @Autowired
    private EquipmentRepository eqRepository;

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
                softly.assertThat(card.getText()) // Маппинг из поля description
                        .as("Text (описание) не должен быть null")
                        .isNotNull();
                softly.assertThat(card.getImage()) // Маппинг из поля photo_path
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