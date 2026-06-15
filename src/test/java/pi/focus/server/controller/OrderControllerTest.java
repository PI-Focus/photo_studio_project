package pi.focus.server.controller;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.security.CustomUserDetails;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IOrderFacade;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.core.service.api.IStaticDataService;
import pi.focus.server.service.models.CalendarDto;
import pi.focus.server.service.models.OrderDto;
import pi.focus.server.service.models.OrderStatusDto;
import tools.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@DisplayName("Тесты для OrderController")
@WithMockUser
@SuppressWarnings({"PMD.LawOfDemeter","PMD.LongVariable", "PMD.AvoidDuplicateLiterals", "PMD.CouplingBetweenObjects"})
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IRoomService roomService;

    @MockitoBean
    private IEquipmentService equipmentService;

    @MockitoBean
    private IPhotographerService photographerService;

    @MockitoBean
    private IOrderFacade orderService;

    @MockitoBean
    private IStaticDataService staticDataService;

    private static final String VALID_UUID = "8718f425-0ebe-48aa-9127-4541ed29524c";
    private static final String VALID_DATE = "2026-06-15";

    @BeforeEach
    void setUpMocks() {
        when(staticDataService.getInfo()).thenReturn(mock(IInfoContext.class, RETURNS_DEEP_STUBS));
    }

    private CustomUserDetails createTestUserDetails() {
        return new CustomUserDetails(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd"),
                "testuser",
                "test@example.com",
                "81111111111"
        );
    }

    private List<List<Integer>> createEmptyCalendar() {
        List<List<Integer>> calendar = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(0);
            }
            calendar.add(row);
        }
        return calendar;
    }

    @Nested
    @DisplayName("GET /order/calendar/{id}")
    class CalendarEndpoint {

        @Test
        @DisplayName("Должен вернуть 200 OK и корректный JSON при валидных параметрах")
        void shouldReturnCalendarWithValidParameters() throws Exception {
            List<List<Integer>> calendarData = createEmptyCalendar();
            ICalendar mockCalendar = new CalendarDto(calendarData);

            UUID uuid = UUID.fromString(VALID_UUID);
            LocalDate date = LocalDate.parse(VALID_DATE);

            when(roomService.getRoomCalendar(uuid, date)).thenReturn(mockCalendar);

            MvcResult result = mockMvc.perform(get("/order/calendar/" + VALID_UUID)
                            .param("date", VALID_DATE)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.ROWS", is(14)))
                    .andExpect(jsonPath("$.COLUMNS", is(7)))
                    .andExpect(jsonPath("$.calendar", hasSize(14)))
                    .andReturn();

            assertNotNull(result.getResponse().getContentAsString(),
                    "Тело ответа не должно быть пустым при успешном запросе календаря");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "not-a-uuid",
                "12345",
                "8718f425-0ebe-48aa-9127"
        })
        @DisplayName("Должен вернуть 400 Bad Request при невалидном UUID")
        void shouldReturnBadRequestForInvalidUuid(String invalidUuid) throws Exception {
            MvcResult result = mockMvc.perform(get("/order/calendar/" + invalidUuid)
                            .param("date", VALID_DATE))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400 для невалидного UUID: " + invalidUuid);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "not-a-date",
                "2026-13-45",
                "15-06-2026",
                "2026/06/15"
        })
        @DisplayName("Должен вернуть 400 Bad Request при невалидной дате")
        void shouldReturnBadRequestForInvalidDate(String invalidDate) throws Exception {
            MvcResult result = mockMvc.perform(get("/order/calendar/" + VALID_UUID)
                            .param("date", invalidDate))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400 для невалидной даты: " + invalidDate);
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если параметр date отсутствует")
        void shouldReturnBadRequestWhenDateParamIsMissing() throws Exception {
            MvcResult result = mockMvc.perform(get("/order/calendar/" + VALID_UUID))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400, если обязательный параметр date не передан");
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если комната не найдена (сервис вернул null)")
        void shouldReturnBadRequestWhenRoomNotFound() throws Exception {
            UUID uuid = UUID.fromString(VALID_UUID);
            LocalDate date = LocalDate.parse(VALID_DATE);

            when(roomService.getRoomCalendar(uuid, date)).thenReturn(null);

            MvcResult result = mockMvc.perform(get("/order/calendar/" + VALID_UUID)
                            .param("date", VALID_DATE))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400, если roomService вернул null");
        }
    }

    @Nested
    @DisplayName("GET /order/equipment")
    class EquipmentEndpoint {

        @Test
        @DisplayName("Должен вернуть 200 OK и список оборудования")
        void shouldReturnEquipmentList() throws Exception {
            Equipment equipment1 = mock(Equipment.class);
            Equipment equipment2 = mock(Equipment.class);
            List<Equipment> equipmentList = List.of(equipment1, equipment2);

            when(equipmentService.getEquipment()).thenReturn(equipmentList);

            MvcResult result = mockMvc.perform(get("/order/equipment")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andReturn();

            assertNotNull(result.getResponse().getContentAsString(),
                    "Тело ответа не должно быть пустым при запросе списка оборудования");
        }

        @Test
        @DisplayName("Должен вернуть 200 OK и пустой массив, если оборудования нет")
        void shouldReturnEmptyListWhenNoEquipment() throws Exception {
            when(equipmentService.getEquipment()).thenReturn(List.of());

            MvcResult result = mockMvc.perform(get("/order/equipment")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andReturn();

            assertEquals("[]", result.getResponse().getContentAsString(),
                    "При отсутствии оборудования должен вернуться пустой JSON-массив");
        }
    }

    @Nested
    @DisplayName("GET /order/photographers")
    class PhotographersEndpoint {

        private static final String VALID_START = "2026-06-15T10:00:00";
        private static final String VALID_END = "2026-06-15T14:00:00";

        @Test
        @DisplayName("Должен вернуть 200 OK и список фотографов при валидных датах")
        @SuppressWarnings("unchecked")
        void shouldReturnPhotographersListWithValidDates() throws Exception {
            Photographer p1 = mock(Photographer.class);
            Photographer p2 = mock(Photographer.class);
            List<Photographer> photographers = List.of(p1, p2);

            when(photographerService.getPhotographersByTime(any(Range.class))).thenReturn(photographers);

            MvcResult result = mockMvc.perform(get("/order/photographers")
                            .param("start", VALID_START)
                            .param("end", VALID_END)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andReturn();

            assertNotNull(result.getResponse().getContentAsString(),
                    "Тело ответа не должно быть пустым при запросе списка фотографов");
        }

        @Test
        @DisplayName("Должен передать корректный Range в сервис с правильными границами")
        @SuppressWarnings("unchecked")
        void shouldPassCorrectRangeToService() throws Exception {
            when(photographerService.getPhotographersByTime(any(Range.class))).thenReturn(List.of());

            mockMvc.perform(get("/order/photographers")
                    .param("start", VALID_START)
                    .param("end", VALID_END)).andReturn();

            assertSoftly(softly -> {
                @SuppressWarnings("unchecked")
                ArgumentCaptor<Range<LocalDateTime>> rangeCaptor = ArgumentCaptor.forClass(Range.class);
                verify(photographerService).getPhotographersByTime(rangeCaptor.capture());

                Range<LocalDateTime> capturedRange = rangeCaptor.getValue();

                softly.assertThat(capturedRange.lower())
                        .as("Нижняя граница Range должна совпадать с параметром start")
                        .isEqualTo(LocalDateTime.parse(VALID_START));
                softly.assertThat(capturedRange.upper())
                        .as("Верхняя граница Range должна совпадать с параметром end")
                        .isEqualTo(LocalDateTime.parse(VALID_END));
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "not-a-date",
                "2026-13-45T10:00:00",
                "15-06-2026 10:00:00"
        })
        @DisplayName("Должен вернуть 400 Bad Request при невалидной дате start или end")
        void shouldReturnBadRequestForInvalidDates(String invalidDate) throws Exception {
            MvcResult result = mockMvc.perform(get("/order/photographers")
                            .param("start", invalidDate)
                            .param("end", VALID_END))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400 для невалидной даты start: " + invalidDate);
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если параметр start отсутствует")
        void shouldReturnBadRequestWhenStartIsMissing() throws Exception {
            MvcResult result = mockMvc.perform(get("/order/photographers")
                            .param("end", VALID_END))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400, если обязательный параметр start не передан");
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если параметр end отсутствует")
        void shouldReturnBadRequestWhenEndIsMissing() throws Exception {
            MvcResult result = mockMvc.perform(get("/order/photographers")
                            .param("start", VALID_START))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, result.getResponse().getStatus(),
                    "Статус должен быть 400, если обязательный параметр end не передан");
        }
    }

    @Nested
    @DisplayName("POST /order/current")
    class PostCurrentOrderEndpoint {

        private OrderStatusDto createTestOrderStatus() {
            OrderDto testOrder = new OrderDto(
                    LocalDateTime.of(2026, 6, 15, 15, 0),
                    LocalDateTime.of(2026, 6, 15, 17, 0),
                    UUID.fromString("692d3820-d762-436a-93ae-aaa1c7d2c1f5"),
                    List.of(),
                    150_000
            );

            return new OrderStatusDto(
                    UUID.fromString("8718f425-0ebe-48aa-9127-4541ed29524c"),
                    testOrder
            );
        }

        @Test
        @DisplayName("Должен вернуть 200 OK при валидном JSON и validateStatus = 0")
        void shouldReturnOkWhenValidationPasses() throws Exception {
            OrderStatusDto testDto = createTestOrderStatus();
            String json = objectMapper.writeValueAsString(testDto);

            when(orderService.validateOrderStatus(any(IOrderStatus.class))).thenReturn(0);

            MvcResult result = mockMvc.perform(post("/order/current")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();

            assertSoftly(softly -> {
                String content = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);

                softly.assertThat(content)
                        .as("Тело ответа не должно быть пустым")
                        .isNotBlank();
            });
        }

        @Test
        @DisplayName("Должен вернуть 202 Accepted при валидном JSON и validateStatus = 1")
        void shouldReturnAcceptedWhenValidationHasWarnings() throws Exception {
            OrderStatusDto testDto = createTestOrderStatus();
            String json = objectMapper.writeValueAsString(testDto);

            when(orderService.validateOrderStatus(any(IOrderStatus.class))).thenReturn(1);

            MvcResult result = mockMvc.perform(post("/order/current")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isAccepted())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 202 Accepted при предупреждениях валидации")
                    .isEqualTo(202));
        }

        @Test
        @DisplayName("Должен вернуть 422 Unprocessable Content при валидном JSON и validateStatus = 2")
        void shouldReturnUnprocessableWhenValidationFails() throws Exception {
            OrderStatusDto testDto = createTestOrderStatus();
            String json = objectMapper.writeValueAsString(testDto);

            when(orderService.validateOrderStatus(any(IOrderStatus.class))).thenReturn(2);

            MvcResult result = mockMvc.perform(post("/order/current")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnprocessableContent())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 422 при ошибках валидации")
                    .isEqualTo(422));
        }

        @Test
        @DisplayName("Должен вернуть 422 Unprocessable Content при невалидном JSON")
        void shouldReturnUnprocessableForInvalidJson() throws Exception {
            MvcResult result = mockMvc.perform(post("/order/current")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}"))
                    .andExpect(status().isUnprocessableContent())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 422 при невалидном JSON")
                    .isEqualTo(422));
        }
    }

    @Nested
    @DisplayName("POST /order/confirm")
    class ConfirmOrderEndpoint {

        private OrderStatusDto createTestOrderStatus() {
            OrderDto testOrder = new OrderDto(
                    LocalDateTime.of(2026, 6, 15, 15, 0),
                    LocalDateTime.of(2026, 6, 15, 17, 0),
                    UUID.fromString("692d3820-d762-436a-93ae-aaa1c7d2c1f5"),
                    List.of(),
                    150_000
            );

            return new OrderStatusDto(
                    UUID.fromString("8718f425-0ebe-48aa-9127-4541ed29524c"),
                    testOrder
            );
        }

        @Test
        @DisplayName("Должен вернуть 200 OK при валидном JSON и validateStatus = 0")
        void shouldReturnOkWhenConfirmationPasses() throws Exception {
            OrderStatusDto testDto = createTestOrderStatus();
            String json = objectMapper.writeValueAsString(testDto);
            CustomUserDetails userDetails = createTestUserDetails();

            when(orderService.validateOrderStatus(any(IOrderStatus.class))).thenReturn(0);

            MvcResult result = mockMvc.perform(post("/order/confirm")
                            .with(csrf())
                            .with(user(userDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();

            assertSoftly(softly -> {
                String content = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);

                softly.assertThat(content)
                        .as("Тело ответа не должно быть пустым")
                        .isNotBlank();
            });
        }

        @Test
        @DisplayName("Должен вернуть 202 Accepted при валидном JSON и validateStatus = 1")
        void shouldReturnAcceptedWhenConfirmationHasWarnings() throws Exception {
            OrderStatusDto testDto = createTestOrderStatus();
            String json = objectMapper.writeValueAsString(testDto);
            CustomUserDetails userDetails = createTestUserDetails();

            when(orderService.validateOrderStatus(any(IOrderStatus.class))).thenReturn(1);

            MvcResult result = mockMvc.perform(post("/order/confirm")
                            .with(csrf())
                            .with(user(userDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isAccepted())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 202 Accepted при предупреждениях подтверждения")
                    .isEqualTo(202));
        }

        @Test
        @DisplayName("Должен вернуть 422 Unprocessable Content при валидном JSON и validateStatus = 2")
        void shouldReturnUnprocessableWhenConfirmationFails() throws Exception {
            OrderStatusDto testDto = createTestOrderStatus();
            String json = objectMapper.writeValueAsString(testDto);
            CustomUserDetails userDetails = createTestUserDetails();

            when(orderService.validateOrderStatus(any(IOrderStatus.class))).thenReturn(2);

            MvcResult result = mockMvc.perform(post("/order/confirm")
                            .with(csrf())
                            .with(user(userDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnprocessableContent())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 422 при ошибках подтверждения")
                    .isEqualTo(422));
        }

        @Test
        @DisplayName("Должен вернуть 422 Unprocessable Content при невалидном JSON")
        void shouldReturnUnprocessableForInvalidJsonInConfirm() throws Exception {
            MvcResult result = mockMvc.perform(post("/order/confirm")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}"))
                    .andExpect(status().isUnprocessableContent())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 422 при невалидном JSON в подтверждении")
                    .isEqualTo(422));
        }
    }
}