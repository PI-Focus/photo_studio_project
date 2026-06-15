package pi.focus.server.core.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pi.focus.server.core.domain.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сущность пользователя системы.
 * Содержит учетные данные для авторизации, контактную информацию 
 * и определяет уровень прав доступа (роль) в приложении.
 */
@Entity
@Table(name = "users")
public class UserEntity {
    /** Уникальный идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Уникальный логин пользователя, используемый для входа в систему */
    @Column(name = "login", nullable = false, length = 32, unique = true)
    private String login;

    /** Контактный номер телефона пользователя */
    @Column(name = "phone_number", length = 11, unique = true)
    String phoneNumber;

    /** Адрес электронной почты пользователя для связи и уведомлений */
    @Column(name = "email", unique = true)
    String email;

    /** Хэшированный пароль пользователя для аутентификации */
    @Column(name = "password", nullable = false)
    private String password;

    /** Роль пользователя в системе */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", nullable = false)
    private UserRole role;

    /** Список всех бронирований, совершенных данным пользователем */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations = new ArrayList<>();

    /** Конструктор для спецификации JPA */
    public UserEntity() {
    }

    /**
     * Конструктор для создания полной записи о пользователе.
     * 
     * @param id уникальный идентификатор
     * @param login логин
     * @param phoneNumber номер телефона
     * @param email электронная почта
     * @param password пароль 
     * @param role роль в системе
     * @param reservations история бронирований
     */
    public UserEntity(
            UUID id,
            String login,
            String phoneNumber,
            String email,
            String password,
            UserRole role,
            List<ReservationEntity> reservations
    ) {
        this.id = id;
        this.login = login;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.reservations = reservations;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
}