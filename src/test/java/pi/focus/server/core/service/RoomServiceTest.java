package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.core.repository.RoomRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SuppressWarnings({"PMD.LawOfDemeter"})
class RoomServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

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
    @DisplayName("Должен корректно мапить поля и генерировать кастомные ссылки")
    void shouldMapFieldsAndConstructLinksCorrectly() {
        IPhotoroomsContext context = roomService.getPhotoroomsContext();
        List<? extends IDataCard> cards = context.getPhotorooms();

        assertSoftly(softly -> {
            softly.assertThat(cards)
                    .as("Список карточек комнат должен содержать 3 элемента")
                    .hasSize(3);

            IDataCard zal1Card = cards.stream()
                    .filter(card -> "Зал 1".equals(card.getTitle()))
                    .findFirst()
                    .orElseThrow();

            softly.assertThat(zal1Card.getTitle())
                    .as("Title для 'Зал 1' должен корректно замапиться")
                    .isEqualTo("Зал 1");

            softly.assertThat(zal1Card.getText())
                    .as("Text (описание) не должен быть пустым")
                    .isNotBlank();

            softly.assertThat(zal1Card.getImage())
                    .as("Должна браться первая фотография из списка")
                    .isNotNull();

            softly.assertThat(zal1Card.getLinkUrl())
                    .as("Ссылка должна формироваться как 'photorooms/' + id")
                    .isEqualTo("photorooms/8718f425-0ebe-48aa-9127-4541ed29524c");
        });
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