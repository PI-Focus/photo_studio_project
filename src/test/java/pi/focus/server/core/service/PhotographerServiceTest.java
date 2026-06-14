package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.models.IImagedTextCard;
import pi.focus.server.api.models.ITextCard;
import pi.focus.server.core.repository.PhotographerRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.LongVariable"})
class PhotographerServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotographerRepository photographerRepository;

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
    @DisplayName("Должен корректно мапить поля из Entity в DTO (включая новую цену)")
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