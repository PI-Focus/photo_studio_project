package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pi.focus.server.api.context.IBaseContext;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.core.exception.StaticDataLoadingException;
import pi.focus.server.core.json.JsonMapper;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StaticDataServiceTest {

    private final StaticDataService service = new StaticDataService();
    private final IBaseContext baseContext = service.getBaseContext();

    @Test
    @DisplayName("Должен успешно загрузить все данные, если JSON файлы валидны")
    @SuppressWarnings("PMD.LawOfDemeter")
    void shouldLoadAllDataSuccessfully() {
        IInfoContext info = service.getInfo();
        assertSoftly(sofly -> {
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
        assertSoftly(softly->{
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
    @DisplayName("Должен выбросить StaticDataLoadingException, если JSON битый")
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    void shouldThrowExceptionWhenJsonIsMalformed() throws IOException {
        try (MockedStatic<JsonMapper> mockedJsonMapper = Mockito.mockStatic(JsonMapper.class)) {

            ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
            mockedJsonMapper.when(JsonMapper::getInstance).thenReturn(mockObjectMapper);

            when(mockObjectMapper.readValue(any(InputStream.class), (Class<?>) any(Class.class)))
                    .thenThrow(new IOException("Bad JSON"));

            assertThatThrownBy(service::getInfo)
                    .isInstanceOf(StaticDataLoadingException.class)
                    .hasMessageContaining("File load or parsing error");
        }
    }


    @Test
    @DisplayName("Должен выбросить исключение, если base.json содержит битый JSON")
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    void shouldThrowExceptionWhenBaseJsonIsMalformed() throws IOException {
        try (MockedStatic<JsonMapper> mockedJsonMapper = Mockito.mockStatic(JsonMapper.class)) {
            ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
            mockedJsonMapper.when(JsonMapper::getInstance).thenReturn(mockObjectMapper);

            when(mockObjectMapper.readValue(any(InputStream.class), (Class<?>) any(Class.class)))
                    .thenThrow(new IOException("Bad JSON"));

            assertThatThrownBy(service::getBaseContext)
                    .isInstanceOf(StaticDataLoadingException.class)
                    .hasMessageContaining("File load or parsing error: base.json");
        }
    }

}


