package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pi.focus.server.AbstractIntegrationTest;
import pi.focus.server.core.domain.User;
import pi.focus.server.core.domain.UserRole;
import pi.focus.server.core.entity.UserEntity;
import pi.focus.server.core.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final String login = "test_user";
    private final String password = "test_password";
    private final String phoneNumber = "79991234567";
    private final String email = "test@example.com";

    @Test
    @DisplayName("Должен успешно создать пользователя и сохранить все поля в БД")
    void shouldCreateUserSuccessfully() {
        User newUser = new User(null, login, phoneNumber, email, password, UserRole.USER);

        boolean result = userService.createUser(newUser);

        UserEntity savedUser = userRepository.findByLogin(login).orElseThrow();

        assertSoftly(softly -> {
            softly.assertThat(result).isTrue();
            softly.assertThat(savedUser.getId())
                    .as("ID должен быть сгенерирован автоматически")
                    .isNotNull();
            softly.assertThat(savedUser.getLogin()).isEqualTo(login);
            softly.assertThat(savedUser.getPhoneNumber()).isEqualTo(phoneNumber);
            softly.assertThat(savedUser.getEmail()).isEqualTo(email);
            softly.assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
            softly.assertThat(savedUser.getPassword())
                    .as("Пароль должен быть захеширован")
                    .isNotEqualTo(password);
        });
    }

    @Test
    @DisplayName("Должен вернуть false, если логин уже существует в БД")
    void shouldReturnFalseIfLoginAlreadyExists() {
        User user1 = new User(null, "login1", phoneNumber, email, password, UserRole.USER);
        userService.createUser(user1);
        boolean result = userService.createUser(user1);
        assertSoftly(softly -> {
            softly.assertThat(result)
                    .as("Должен вернуть false при дубликате логина")
                    .isFalse();

            UserEntity savedUser = userRepository.findByLogin("login1").orElseThrow();
            softly.assertThat(savedUser.getPhoneNumber())
                    .as("Должен сохраниться номер первого пользователя")
                    .isEqualTo("82222222222");
        });
    }

    @Test
    @DisplayName("Должен корректно сохранить роль ADMIN")
    void shouldSaveAdminRoleCorrectly() {
        User adminUser = new User(null, "admin_test", phoneNumber, email, password, UserRole.ADMIN);

        userService.createUser(adminUser);

        UserEntity savedUser = userRepository.findByLogin("admin_test").orElseThrow();

        assertThat(savedUser.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("Должен сохранить пароль в хешированном виде, а не в открытом")
    void shouldSaveHashedPasswordInsteadOfPlainText() {
        User newUser = new User(null, "hash_test", phoneNumber, email, password, UserRole.USER);

        userService.createUser(newUser);

        UserEntity savedUser = userRepository.findByLogin("hash_test").orElseThrow();

        assertSoftly(softly -> {
            softly.assertThat(savedUser.getPassword())
                    .as("Пароль в базе данных не должен быть в открытом виде")
                    .isNotEqualTo(password);

            softly.assertThat(savedUser.getPassword())
                    .as("Пароль должен быть BCrypt хешем")
                    .startsWith("$2a$");
        });
    }

    @Test
    @DisplayName("Должен сохранить номер телефона")
    void shouldSavePhoneNumber() {
        User newUser = new User(null, "phone_test", "89161234567", email, password, UserRole.USER);

        userService.createUser(newUser);

        UserEntity savedUser = userRepository.findByLogin("phone_test").orElseThrow();

        assertThat(savedUser.getPhoneNumber())
                .as("Номер телефона должен быть сохранен")
                .isEqualTo("89161234567");
    }

    @Test
    @DisplayName("Должен сохранить email")
    void shouldSaveEmail() {
        User newUser = new User(null, "email_test", phoneNumber, "user@domain.com", password, UserRole.USER);

        userService.createUser(newUser);

        UserEntity savedUser = userRepository.findByLogin("email_test").orElseThrow();

        assertThat(savedUser.getEmail())
                .as("Email должен быть сохранен")
                .isEqualTo("user@domain.com");
    }



    @Test
    @DisplayName("Должен создать пользователя с null email и phoneNumber")
    void shouldCreateUserWithNullOptionalFields() {
        User newUser = new User(null, "optional_null", null, null, password, UserRole.USER);

        boolean result = userService.createUser(newUser);

        UserEntity savedUser = userRepository.findByLogin("optional_null").orElseThrow();

        assertSoftly(softly -> {
            softly.assertThat(result).isTrue();
            softly.assertThat(savedUser.getPhoneNumber()).isNull();
            softly.assertThat(savedUser.getEmail()).isNull();
        });
    }
}