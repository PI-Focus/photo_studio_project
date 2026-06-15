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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.core.security.CustomUserDetails;
import pi.focus.server.core.service.api.IUserService;

import pi.focus.server.service.models.CalendarDto;
import pi.focus.server.service.models.ReservationDto;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings({"PMD.LawOfDemeter", "PMD.LongVariable", "PMD.AvoidDuplicateLiterals", "PMD.CouplingBetweenObjects", "PMD.TooManyMethods"})
class ProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    private static final UUID USER_ID = UUID.fromString("3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd");
    private static final UUID OTHER_USER_ID = UUID.fromString("8718f425-0ebe-48aa-9127-4541ed29524c");
    private static final String USER_LOGIN = "testuser";
    private static final String VALID_DATE = "2026-06-15";
    private static final String ORDERS_PATH = "/profile/{id}/orders";
    private static final String CALENDAR_PATH = "/profile/{id}/orders/calendar";

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        ProfileController controller = new ProfileController(userService);
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
        @DisplayName("Должен вернуть view 'pages/profile', если UUID совпадает с ID текущего пользователя")
        void shouldReturnProfileViewWhenUserIdMatches() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(ORDERS_PATH, USER_ID))
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(Objects.requireNonNull(result.getModelAndView()).getViewName())
                    .as("Должен вернуться view 'pages/profile'")
                    .isEqualTo("pages/profile"));
        }

        @Test
        @DisplayName("Должен вернуть redirect, если UUID не совпадает с ID пользователя")
        void shouldRedirectWhenUserIdDoesNotMatch() throws Exception {
            CustomUserDetails userDetails = createTestUserDetails();
            authenticateUser(userDetails);

            MvcResult result = mockMvc.perform(get(ORDERS_PATH, OTHER_USER_ID))
                    .andReturn();

            assertSoftly(softly -> softly.assertThat(result.getModelAndView().getViewName())
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

            assertSoftly(softly -> softly.assertThat(result.getModelAndView().getViewName())
                    .as("Должен вернуться redirect")
                    .startsWith("redirect:"));
        }
    }

    // ==================== ТЕСТЫ ДЛЯ GET /{id}/orders/calendar ====================

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
}