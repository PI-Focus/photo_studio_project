package pi.focus.server.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.security.CustomUserDetails;
import pi.focus.server.core.service.api.IReservationService;
import pi.focus.server.core.service.api.IUserService;
import pi.focus.server.service.models.CalendarDto;
import pi.focus.server.service.models.CredentialsDto;
import pi.focus.server.service.models.OrderIdDto;
import pi.focus.server.service.models.ReservationDto;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.LongVariable", "PMD.AvoidDuplicateLiterals"})
class ProfileControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private IUserService userService;

    @Mock
    private IReservationService reservationService;

    private static final UUID USER_ID = UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd");
    private static final UUID OTHER_USER_ID = UUID.fromString("8718f425-0ebe-48aa-9127-4541ed29524c");
    private static final UUID ORDER_ID = UUID.fromString("56190bf3-92e4-450f-8fed-f2298069c82a");
    private static final String USER_LOGIN = "testuser";
    private static final String VALID_DATE = "2026-06-15";
    private static final String ORDERS_PATH = "/profile/{id}/orders";
    private static final String CALENDAR_PATH = "/profile/{id}/orders/calendar";
    private static final String CREDENTIALS_PATH = "/profile/{id}/credentials";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        ProfileController controller = new ProfileController(userService, reservationService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private CustomUserDetails createTestUserDetails() {
        return new CustomUserDetails(
                USER_LOGIN,
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                USER_ID,
                USER_LOGIN,
                "test@example.com",
                "81111111111"
        );
    }

    private void authenticateUser(CustomUserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
    }

    private ICalendar createEmptyCalendar() {
        List<List<Integer>> calendar = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(-1);
            }
            calendar.add(row);
        }
        return new CalendarDto(calendar);
    }

    @Nested
    @DisplayName("GET /profile/{id}/orders (View)")
    class GetOrdersViewEndpoint {
        @Test
        @DisplayName("Должен вернуть redirect, если UUID не совпадает с ID пользователя")
        void shouldRedirectWhenUserIdDoesNotMatch() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(ORDERS_PATH, OTHER_USER_ID))
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(Objects.requireNonNull(result.getModelAndView()).getViewName())
                    .as("Должен вернуться redirect")
                    .startsWith("redirect:"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"not-a-uuid", "12345", "8718f425-0ebe-48aa-9127"})
        @DisplayName("Должен вернуть redirect при невалидном UUID")
        void shouldRedirectForInvalidUuid(String invalidUuid) throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(ORDERS_PATH, invalidUuid))
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(Objects.requireNonNull(result.getModelAndView()).getViewName())
                    .as("Должен вернуться redirect")
                    .startsWith("redirect:"));
        }
    }

    @Nested
    @DisplayName("GET /profile/{id}/orders/calendar (JSON)")
    class GetCalendarJsonEndpoint {

        @Test
        @DisplayName("Должен вернуть 200 OK и корректный JSON при валидных параметрах")
        void shouldReturnCalendarWithValidParameters() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            ICalendar mockCalendar = createEmptyCalendar();
            List<ReservationDto> reservations = List.of(
                    new ReservationDto(
                            UUID.randomUUID(),
                            "Зал 1",
                            LocalDateTime.of(2026, 6, 15, 10, 0),
                            LocalDateTime.of(2026, 6, 15, 12, 0)
                    )
            );

            when(userService.getUserCalendar(eq(USER_ID), any(LocalDate.class))).thenReturn(mockCalendar);
            when(userService.getUserReservationDtos(eq(USER_ID), any(LocalDate.class))).thenReturn(reservations);

            MvcResult result = mockMvc.perform(get(CALENDAR_PATH, USER_ID)
                            .param("date", VALID_DATE)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            assertSoftly(softly -> {
                String content = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
                softly.assertThat(content)
                        .as("Тело ответа не должно быть пустым при успешном запросе календаря")
                        .isNotBlank();
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"not-a-uuid", "12345", "8718f425-0ebe-48aa-9127"})
        @DisplayName("Должен вернуть 400 Bad Request при невалидном UUID")
        void shouldReturnBadRequestForInvalidUuid(String invalidUuid) throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(CALENDAR_PATH, invalidUuid)
                            .param("date", VALID_DATE))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400 для невалидного UUID")
                    .isEqualTo(400));
        }

        @ParameterizedTest
        @ValueSource(strings = {"not-a-date", "2026-13-45", "15-06-2026", "2026/06/15"})
        @DisplayName("Должен вернуть 400 Bad Request при невалидной дате")
        void shouldReturnBadRequestForInvalidDate(String invalidDate) throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(CALENDAR_PATH, USER_ID)
                            .param("date", invalidDate))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400 для невалидной даты")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если дата больше чем +35 дней от сегодня")
        void shouldReturnBadRequestForDateBeyond35Days() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);
            String futureDate = LocalDate.now().plusDays(36).toString();

            MvcResult result = mockMvc.perform(get(CALENDAR_PATH, USER_ID)
                            .param("date", futureDate))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400 для даты за пределами +35 дней")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если сервис вернул null (пользователь не найден)")
        void shouldReturnBadRequestWhenServiceReturnsNull() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            when(userService.getUserCalendar(eq(USER_ID), any(LocalDate.class))).thenReturn(null);

            MvcResult result = mockMvc.perform(get(CALENDAR_PATH, USER_ID)
                            .param("date", VALID_DATE))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400, если сервис вернул null")
                    .isEqualTo(400));
        }
    }

    @Nested
    @DisplayName("DELETE /profile/{id}/orders")
    class DeleteOrderEndpoint {

        @Test
        @DisplayName("Должен вернуть 200 OK при успешном удалении заказа")
        void shouldReturnOkWhenOrderDeletedSuccessfully() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            OrderIdDto orderIdDto = new OrderIdDto(ORDER_ID);
            String json = objectMapper.writeValueAsString(orderIdDto);

            when(reservationService.deleteOrderById(eq(ORDER_ID))).thenReturn(true);

            MvcResult result = mockMvc.perform(delete(ORDERS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 200 при успешном удалении")
                    .isEqualTo(200));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если UUID не совпадает с ID пользователя")
        void shouldReturnBadRequestWhenUserIdDoesNotMatch() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            OrderIdDto orderIdDto = new OrderIdDto(ORDER_ID);
            String json = objectMapper.writeValueAsString(orderIdDto);

            MvcResult result = mockMvc.perform(delete(ORDERS_PATH, OTHER_USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400, если UUID не совпадает")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request при невалидном JSON")
        void shouldReturnBadRequestForInvalidJson() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(delete(ORDERS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400 при невалидном JSON")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если сервис не смог удалить заказ")
        void shouldReturnBadRequestWhenServiceFailsToDelete() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            OrderIdDto orderIdDto = new OrderIdDto(ORDER_ID);
            String json = objectMapper.writeValueAsString(orderIdDto);

            when(reservationService.deleteOrderById(eq(ORDER_ID))).thenReturn(false);

            MvcResult result = mockMvc.perform(delete(ORDERS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400, если сервис вернул false")
                    .isEqualTo(400));
        }
    }

    @Nested
    @DisplayName("GET /profile/{id}/credentials")
    class GetCredentialsEndpoint {

        @Test
        @DisplayName("Должен вернуть 200 OK и учетные данные пользователя")
        void shouldReturnCredentialsSuccessfully() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(CREDENTIALS_PATH, USER_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            assertSoftly(softly -> {
                String content = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
                softly.assertThat(content)
                        .as("Тело ответа не должно быть пустым")
                        .contains(USER_LOGIN);
            });
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если UUID не совпадает с ID пользователя")
        void shouldReturnBadRequestWhenUserIdDoesNotMatch() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(CREDENTIALS_PATH, OTHER_USER_ID))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400, если UUID не совпадает")
                    .isEqualTo(400));
        }

        @ParameterizedTest
        @ValueSource(strings = {"not-a-uuid", "12345", "8718f425-0ebe-48aa-9127"})
        @DisplayName("Должен вернуть 400 Bad Request при невалидном UUID")
        void shouldReturnBadRequestForInvalidUuid(String invalidUuid) throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(CREDENTIALS_PATH, invalidUuid))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400 для невалидного UUID")
                    .isEqualTo(400));
        }
    }

    @Nested
    @DisplayName("POST /profile/{id}/credentials")
    class PostCredentialsEndpoint {

        @Test
        @DisplayName("Должен вернуть 200 OK при успешном обновлении учетных данных")
        void shouldReturnOkWhenCredentialsUpdatedSuccessfully() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            CredentialsDto credentialsDto = new CredentialsDto(
                    "newlogin",
                    null,
                    "89999999999",
                    "newemail@example.com",
                    "newpassword"
            );
            String json = objectMapper.writeValueAsString(credentialsDto);

            when(userService.updateUser(any())).thenReturn("");

            MvcResult result = mockMvc.perform(post(CREDENTIALS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();

            assertSoftly(softly -> {
                String content = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
                softly.assertThat(content)
                        .as("Тело ответа должно содержать обновленные данные")
                        .isNotBlank();
            });
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если UUID не совпадает с ID пользователя")
        void shouldReturnBadRequestWhenUserIdDoesNotMatch() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            CredentialsDto credentialsDto = new CredentialsDto(
                    "newlogin",
                    null,
                    "89999999999",
                    "newemail@example.com",
                    "newpassword"
            );
            String json = objectMapper.writeValueAsString(credentialsDto);

            MvcResult result = mockMvc.perform(post(CREDENTIALS_PATH, OTHER_USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400, если UUID не совпадает")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request при невалидном JSON")
        void shouldReturnBadRequestForInvalidJson() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(post(CREDENTIALS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}"))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400 при невалидном JSON")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request, если сервис вернул null")
        void shouldReturnBadRequestWhenServiceReturnsNull() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            CredentialsDto credentialsDto = new CredentialsDto(
                    "newlogin",
                    null,
                    "89999999999",
                    "newemail@example.com",
                    "newpassword"
            );
            String json = objectMapper.writeValueAsString(credentialsDto);

            when(userService.updateUser(any())).thenReturn(null);

            MvcResult result = mockMvc.perform(post(CREDENTIALS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getResponse().getStatus())
                    .as("Статус должен быть 400, если сервис вернул null")
                    .isEqualTo(400));
        }

        @Test
        @DisplayName("Должен вернуть 400 Bad Request с сообщением об ошибке, если сервис вернул ошибку")
        void shouldReturnBadRequestWithErrorMessage() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            CredentialsDto credentialsDto = new CredentialsDto(
                    "newlogin",
                    null,
                    "89999999999",
                    "newemail@example.com",
                    "newpassword"
            );
            String json = objectMapper.writeValueAsString(credentialsDto);

            when(userService.updateUser(any())).thenReturn("Логин уже занят");

            MvcResult result = mockMvc.perform(post(CREDENTIALS_PATH, USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertSoftly(softly -> {
                String content = new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
                softly.assertThat(content)
                        .as("Тело ответа должно содержать сообщение об ошибке")
                        .contains("Логин уже занят");
            });
        }
    }
}