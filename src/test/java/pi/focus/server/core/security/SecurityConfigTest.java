package pi.focus.server.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.thymeleaf.autoconfigure.ThymeleafAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pi.focus.server.api.context.IEquipmentContext;
import pi.focus.server.api.context.IInfoContext;
import pi.focus.server.api.context.IPhotographersContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.controller.HomeController;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.service.api.*;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(
        controllers = HomeController.class,
        excludeAutoConfiguration = ThymeleafAutoConfiguration.class // Исключаем Thymeleaf
)
@Import({
        SecurityConfig.class,
        SecurityConfigAuthorizationTest.MockViewResolverConfig.class // Подключаем заглушку
})
@SuppressWarnings({"PMD.LongVariable", "PMD.AvoidDuplicateLiterals", "PMD.LawOfDemeter"})
class SecurityConfigAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private IStaticDataService staticDataService;

    @MockitoBean
    private IRoomService roomService;

    @MockitoBean
    private IUserService userService;

    @MockitoBean
    private IEquipmentService equipmentService;

    @MockitoBean
    private IPhotographerService photographerService;

    @BeforeEach
    void setUpMocks() {
        when(staticDataService.getInfo()).thenReturn(mock(IInfoContext.class, RETURNS_DEEP_STUBS));
        when(roomService.getPhotoroomsContext()).thenReturn(mock(IPhotoroomsContext.class, RETURNS_DEEP_STUBS));
        when(equipmentService.getEquipmentContext()).thenReturn(mock(IEquipmentContext.class, RETURNS_DEEP_STUBS));
        when(photographerService.getEquipmentContext()).thenReturn(mock(IPhotographersContext.class, RETURNS_DEEP_STUBS));
    }

    @TestConfiguration
    static class MockViewResolverConfig implements WebMvcConfigurer {
        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            registry.viewResolver(new ViewResolver() {
                @Override
                public View resolveViewName(@NotNull String viewName, @NotNull Locale locale) {
                    return new View() {
                        @Override
                        public String getContentType() {
                            return "text/html";
                        }

                        @Override
                        public void render(Map<String, ?> model, @NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                    };
                }
            });
        }
    }

    @Nested
    @DisplayName("Правила авторизации")
    class AuthorizationRules {

        @ParameterizedTest
        @ValueSource(strings = {"/", "/photorooms", "/equipment", "/photographers", "/registration", "/login"})
        @WithAnonymousUser
        @DisplayName("Публичные страницы доступны без авторизации")
        void publicEndpointsShouldBeAccessible(String endpoint) throws Exception {
            MvcResult result = mockMvc.perform(get(endpoint)).andReturn();
            assertEquals(200, result.getResponse().getStatus(),
                    "Публичная страница " + endpoint + " должна быть доступна (статус 200)");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("/profile требует авторизации (302 redirect на /login)")
        void profileShouldRequireAuthentication() throws Exception {
            MvcResult result = mockMvc.perform(get("/profile")).andReturn();
            assertEquals("/login", result.getResponse().getRedirectedUrl(),
                    "Доступ к /profile без авторизации должен перенаправлять на /login");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "/css/style.css",
                "/images/logo.png",
                "/js/app.js"
        })
        @WithAnonymousUser
        @DisplayName("Статические ресурсы не блокируются Spring Security (нет редиректа на /login)")
        void staticResourcesShouldNotBeBlockedBySecurity(String resourcePath) throws Exception {
            MvcResult result = mockMvc.perform(get(resourcePath)).andReturn();
            assertNull(result.getResponse().getRedirectedUrl(),
                    "Статический ресурс " + resourcePath + " не должен блокироваться Spring Security");
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("/profile доступен авторизованному пользователю с ролью USER")
        void profileShouldBeAccessibleForAuthenticatedUser() throws Exception {
            MvcResult result = mockMvc.perform(get("/profile")).andReturn();
            assertEquals(200, result.getResponse().getStatus(),
                    "Страница /profile должна быть доступна авторизованному пользователю");
        }
    }

    @Nested
    @DisplayName("Интеграция с HomeController и previousURI (через Referer)")
    class PreviousUriIntegration {

        @Test
        @WithAnonymousUser
        @DisplayName("Должен сохранить previousUri в сессию при GET /login с заголовком Referer")
        void shouldSavePreviousUriInSession() throws Exception {
            MockHttpSession session = new MockHttpSession();


            mockMvc.perform(get("/login")
                    .header("Referer", "/photorooms")
                    .session(session)).andReturn();

            assertEquals("/photorooms", session.getAttribute("previousUri"),
                    "Атрибут previousUri должен корректно сохраниться в сессии из заголовка Referer");
        }

        @ParameterizedTest
        @ValueSource(strings = {"/login", "/registration"})
        @WithAnonymousUser
        @DisplayName("НЕ должен сохранять previousUri, если Referer указывает на /login или /registration")
        void shouldNotSaveLoginOrRegistrationAsPreviousUri(String forbiddenUri) throws Exception {
            MockHttpSession session = new MockHttpSession();

            mockMvc.perform(get("/login")
                    .header("Referer", forbiddenUri)
                    .session(session)).andReturn();

            assertThat(Optional.ofNullable(session.getAttribute("previousUri")))
                    .as("Атрибут previousUri не должен сохраняться для Referer " + forbiddenUri)
                    .isEmpty();
        }

        @Test
        @DisplayName("Должен перенаправить на previousUri после успешного логина")
        void shouldRedirectToPreviousUriAfterSuccessfulLogin() throws Exception {
            UserEntity userEntity = new UserEntity();
            userEntity.setLogin("validuser");
            userEntity.setPassword("$2a$10$encodedPassword");
            userEntity.setRole(UserRole.USER);
            when(userRepository.findByLogin("validuser")).thenReturn(Optional.of(userEntity));
            when(passwordEncoder.matches("correctPassword", "$2a$10$encodedPassword"))
                    .thenReturn(true);

            MockHttpSession session = new MockHttpSession();


            mockMvc.perform(get("/login")
                    .header("Referer", "/photorooms")
                    .session(session)).andReturn();


            MvcResult result = mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("login", "validuser")
                    .param("password", "correctPassword")
                    .session(session)).andReturn();

            assertEquals("/photorooms", result.getResponse().getRedirectedUrl(),
                    "После успешного логина должен быть редирект на previousUri из сессии");
        }

        @Test
        @DisplayName("Должен перенаправить на главную, если previousUri отсутствует")
        void shouldRedirectToHomeIfNoPreviousUri() throws Exception {
            UserEntity userEntity = new UserEntity();
            userEntity.setLogin("validuser");
            userEntity.setPassword("$2a$10$encodedPassword");
            userEntity.setRole(UserRole.USER);
            when(userRepository.findByLogin("validuser")).thenReturn(Optional.of(userEntity));
            when(passwordEncoder.matches("correctPassword", "$2a$10$encodedPassword"))
                    .thenReturn(true);

            MockHttpSession session = new MockHttpSession();

            MvcResult result = mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("login", "validuser")
                    .param("password", "correctPassword")
                    .session(session)).andReturn();

            assertEquals("/", result.getResponse().getRedirectedUrl(),
                    "При отсутствии previousUri должен быть редирект на главную страницу");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("Spring Security сохраняет SavedRequest при попытке доступа к /profile без авторизации")
        void shouldSaveRequestWhenAccessingProtectedPage() throws Exception {
            MockHttpSession session = new MockHttpSession();

            mockMvc.perform(get("/profile").session(session)).andReturn();

            assertNotNull(session.getAttribute("SPRING_SECURITY_SAVED_REQUEST"),
                    "Spring Security должен сохранить SavedRequest при попытке доступа к защищенной странице");
        }
    }
}