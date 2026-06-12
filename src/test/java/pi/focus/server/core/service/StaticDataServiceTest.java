package pi.focus.server.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.core.exception.StaticDataLoadingException;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings({"PMD.TooManyMethods"})
class StaticDataServiceTest {

    private static final String ABOUT_PATH = "classpath:/data/about.json";
    private static final String RULES_PATH = "classpath:/data/rules.json";
    private static final String PREVIEW_PATH = "classpath:/data/preview.json";
    private static final String BASE_PATH = "classpath:/data/base.json";
    private static final String INVALID_JSON_PATH = "classpath:/test-data/invalid_json";
    private static final String EMPTY_JSON_PATH = "classpath:/test-data/empty_json";
    private static final String MESSAGE_ERROR = "File load or parsing error";

    private StaticDataService service;
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @BeforeEach
    void setup() {
        this.service = new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        );
    }

    @Test
    @DisplayName("Должен успешно загрузить все данные, если JSON файлы валидны")
    @SuppressWarnings("PMD.LawOfDemeter")
    void shouldLoadAllDataSuccessfully() {
        IInfoContext info = service.getInfo();
        assertSoftly(softly -> {
            assertThat(info).isNotNull();
            assertThat(info.getAboutBlock()).isNotNull();
            assertThat(info.getDataTabs()).isNotNull().isNotEmpty();
            assertThat(info.getRentRules()).isNotNull().isNotEmpty();
        });
    }

    @Test
    @DisplayName("Должен успешно загрузить базовые данные из base.json, если JSON валидны")
    @SuppressWarnings("PMD.LawOfDemeter")
    void shouldLoadBaseDataSuccessfully() {
        IBaseContext baseContext = service.getBaseContext();
        assertSoftly(softly -> {
            assertThat(baseContext).isNotNull();
            assertThat(baseContext.getAddress()).isNotNull().isNotEmpty();
            assertThat(baseContext.getInn()).isNotNull().isNotEmpty();
            assertThat(baseContext.getEmail()).isNotNull().isNotEmpty();
            assertThat(baseContext.getPhone()).isNotNull().isNotEmpty();
            assertThat(baseContext.getOgrnip()).isNotNull().isNotEmpty();
            assertThat(baseContext.getCompanyName()).isNotNull().isNotEmpty();
        });
    }

    @Test
    @DisplayName("Должен выбросить исключение, если about.json содержит невалидный JSON")
    void shouldThrowExceptionWhenAboutJsonIsInvalid() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(INVALID_JSON_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOf(StaticDataLoadingException.class)
                .hasMessageContaining(MESSAGE_ERROR);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если rules.json содержит невалидный JSON")
    void shouldThrowExceptionWhenRulesJsonIsInvalid() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(INVALID_JSON_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOf(StaticDataLoadingException.class)
                .hasMessageContaining(MESSAGE_ERROR);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если preview.json содержит невалидный JSON")
    void shouldThrowExceptionWhenPreviewJsonIsInvalid() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(INVALID_JSON_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOf(StaticDataLoadingException.class)
                .hasMessageContaining(MESSAGE_ERROR);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если base.json содержит невалидный JSON")
    void shouldThrowExceptionWhenBaseJsonIsInvalid() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(INVALID_JSON_PATH)
        ))
                .isInstanceOf(StaticDataLoadingException.class)
                .hasMessageContaining(MESSAGE_ERROR);
    }

    @Test
    @DisplayName("Должен выбросить исключение или вернуть null поля при пустом about.json")
    void shouldHandleEmptyAboutJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(EMPTY_JSON_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение или вернуть пустой список при пустом rules.json")
    void shouldHandleEmptyRulesJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(EMPTY_JSON_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение или вернуть пустой список при пустом preview.json")
    void shouldHandleEmptyPreviewJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(EMPTY_JSON_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение или вернуть null поля при пустом base.json")
    void shouldHandleEmptyBaseJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(EMPTY_JSON_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение при отсутствии about.json")
    void shouldHandleExceptionForNotExistingAboutJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource("classpath:/test-data/about_j"),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение при отсутствии rules.json")
    void shouldHandleExceptionForNotExistingRulesJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource("classpath:/test-data/rules.j"),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение при отсутствии preview.json")
    void shouldHandleExceptionForNotExistingPreviewJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource("classpath:/test-data/preview.j"),
                resourceLoader.getResource(BASE_PATH)
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }

    @Test
    @DisplayName("Должен выбросить исключение при отсутствии base.json")
    void shouldHandleExceptionForNotExistingBaseJson() {
        assertThatThrownBy(() -> new StaticDataService(
                resourceLoader.getResource(ABOUT_PATH),
                resourceLoader.getResource(RULES_PATH),
                resourceLoader.getResource(PREVIEW_PATH),
                resourceLoader.getResource("classpath:/test-data/base.j")
        ))
                .isInstanceOfAny(StaticDataLoadingException.class);
    }
}