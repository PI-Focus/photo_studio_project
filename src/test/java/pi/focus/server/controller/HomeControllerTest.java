package pi.focus.server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.security.SecurityConfig;
import pi.focus.server.core.service.api.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
@SuppressWarnings({"PMD.LongVariable", "PMD.AvoidDuplicateLiterals"})
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setLogin("validuser");
        mockUserEntity.setPassword("encodedPassword");
        mockUserEntity.setRole(UserRole.USER);

        when(userRepository.findByLogin(anyString()))
                .thenReturn(Optional.of(mockUserEntity));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);
    }


    @Test
    @DisplayName("Должен отклонить регистрацию, если пароли не совпадают")
    void shouldRejectRegistrationWhenPasswordsDoNotMatch() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "testuser")
                        .param("password", "password123")
                        .param("confirmPassword", "different_password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?error"))
                .andExpect(flash().attribute("error", "Пароли не совпадают"));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Должен отклонить регистрацию, если пароль слишком короткий")
    void shouldRejectRegistrationWhenPasswordTooShort() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "testuser")
                        .param("password", "short")
                        .param("confirmPassword", "short"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?error"))
                .andExpect(flash().attribute("error", "Минимальная длина пароля должна быть 8 символов"));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Должен отклонить регистрацию, если логин содержит недопустимые символы")
    void shouldRejectRegistrationWhenLoginContainsInvalidChars() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "Test_User")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?error"))
                .andExpect(flash().attribute("error",
                        "Логин может содержать только строчные латинские буквы, цифры, символы нижнего подчеркивания и дефиса"));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Должен отклонить регистрацию, если логин не содержит букв")
    void shouldRejectRegistrationWhenLoginHasNoLetters() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "12345")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?error"))
                .andExpect(flash().attribute("error", "Логин должен содержать латинские буквы"));

        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Должен отклонить регистрацию, если логин слишком короткий")
    void shouldRejectRegistrationWhenLoginTooShort() throws Exception {
        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "abc")
                        .param("password", "password123")
                        .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?error"))
                .andExpect(flash().attribute("error", "Минимальная длина логина должна быть 4 символов"));

        verify(userService, never()).createUser(any());
    }



    @Test
    @DisplayName("Должен успешно зарегистрировать пользователя")
    void shouldRegisterUserSuccessfully() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "validuser")
                        .param("password", "validpass123")
                        .param("confirmPassword", "validpass123"))
                .andExpect(status().is3xxRedirection());

        verify(userService).createUser(argThat(user ->
                "validuser".equals(user.login()) &&
                        "validpass123".equals(user.password()) &&
                        user.role() == UserRole.USER
        ));
    }

    @Test
    @DisplayName("Должен отклонить регистрацию, если логин уже существует")
    void shouldRejectRegistrationWhenLoginAlreadyExists() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .param("login", "existinguser")
                        .param("password", "validpass123")
                        .param("confirmPassword", "validpass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration?error"))
                .andExpect(flash().attribute("error", "Пользователь с таким именем уже существует"));

        verify(userService).createUser(any(User.class));
    }
}