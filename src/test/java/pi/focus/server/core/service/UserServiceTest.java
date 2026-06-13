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

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    private final   String login = "test_user";
    private final  String password =  "test_password";


    @Test
    @DisplayName("Должен успешно создать пользователя и сохранить его в БД")
    void shouldCreateUserSuccessfully(){
        User newUser = new User(null ,login ,password, UserRole.USER);
        boolean result = userService.createUser(newUser);
        Optional<UserEntity> savedUser = userRepository.findByLoginIgnoreCase(login);

        assertSoftly(softly -> {
            assertThat(result).isTrue();


            softly.assertThat(savedUser)
                    .as("Пользователь должен быть сохранен в БД")
                    .isPresent()
                    .get()
                    .satisfies(user -> {
                        softly.assertThat(user.getLogin()).isEqualTo(login);
                        softly.assertThat(user.getRole()).isEqualTo(UserRole.USER);
                    });
        });
    }

    @Test
    @DisplayName("Должен вернуть false, если логин уже существует в БД")
    void shouldReturnFalseIfLoginAlreadyExists() {
        User user1 = new User(null, "login1", password, UserRole.USER);
        boolean result  = userService.createUser(user1);
        UserEntity savedUser = userRepository.findByLoginIgnoreCase("login1").orElseThrow();
        assertSoftly(softly->{
            assertThat(result).isFalse();
            assertThat(savedUser.getPassword()).isNotEqualTo(password);
        });

    }

    @Test
    @DisplayName("Должен корректно сохранить роль ADMIN")
    void shouldSaveAdminRoleCorrectly() {
        User adminUser = new User(null, login, password, UserRole.ADMIN);
        userService.createUser(adminUser);
        UserEntity savedUser = userRepository.findByLoginIgnoreCase(login).orElseThrow();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.ADMIN);
    }

//    @Test
//    @DisplayName("Должен сохранить пароль в хешированном виде, а не в открытом")
//    void shouldSaveHashedPasswordInsteadOfPlainText() {
//        User newUser = new User(null, login, password, UserRole.USER);
//        userService.createUser(newUser);
//        UserEntity savedUser = userRepository.findByLoginIgnoreCase(login).orElseThrow();
//        assertSoftly(softly-> {assertThat(savedUser.getPassword())
//                .as("Пароль в базе данных не должен быть в открытом виде")
//                .isNotEqualTo(password);
//          });
//    }
}