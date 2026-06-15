package pi.focus.server.core.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** 
 * Сущность бронирования.
 * Центровой узел, объединяющий информацию о клиенте,
 * забронированном зале, времени аренды и выбранных дополнительных услугах
 */
@Entity
@Table(name = "reservations")
public class ReservationEntity {
    /** Уникальный идентификатор бронирования */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    UUID id;

    /** Пользователь, совершивший бронирование */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    /** Фотозал, выбранный для аренды */
    @ManyToOne
    @JoinColumn(name = "room_id")
    RoomEntity room;

    /** Дата, на которую забронирован зал */
    @Column(name = "day", nullable = false, columnDefinition = "DATE")
    LocalDate day;

    /** Время начала аренды */
    @Column(name = "from_time", nullable = false, columnDefinition = "SMALLINT")
    Short fromTime;

    /** Время окончания аренды */
    @Column(name = "to_time", nullable = false, columnDefinition = "SMALLINT")
    Short toTime;

    /** Список дополнительного оборудования, зарезервированного в рамках данной брони */
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservedEquipmentEntity> reservedEquipments = new ArrayList<>();

    /** Список фотографов, забронированных для проведения съемки в рамках данной брони */
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservedPhotographerEntity> reservedPhotographers = new ArrayList<>();

    /** Конструктор для спецификации JPA */
    public ReservationEntity() {
    }

    /** 
     * Конструктор для создания полной записи о бронировании
     * @param id уникальный идентификатор
     * @param user объект пользователя
     * @param room объект выбранного зала
     * @param day дата съемки
     * @param fromTime час начала
     * @param toTime час окончания
     * @param reservedEquipments список оборудования
     * @param reservedPhotographers список фотографов
     */
    public ReservationEntity(
            UUID id,
            UserEntity user,
            RoomEntity room,
            LocalDate day,
            Short fromTime,
            Short toTime,
            List<ReservedEquipmentEntity> reservedEquipments,
            List<ReservedPhotographerEntity> reservedPhotographers
    ) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.reservedEquipments = reservedEquipments;
        this.reservedPhotographers = reservedPhotographers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Short getFromTime() {
        return fromTime;
    }

    public void setFromTime(Short fromTime) {
        this.fromTime = fromTime;
    }

    public Short getToTime() {
        return toTime;
    }

    public void setToTime(Short toTime) {
        this.toTime = toTime;
    }

    public List<ReservedEquipmentEntity> getReservedEquipments() {
        return reservedEquipments;
    }

    public void setReservedEquipments(List<ReservedEquipmentEntity> reservedEquipments) {
        this.reservedEquipments = reservedEquipments;
    }

    public List<ReservedPhotographerEntity> getReservedPhotographers() {
        return reservedPhotographers;
    }

    public void setReservedPhotographers(List<ReservedPhotographerEntity> reservedPhotographers) {
        this.reservedPhotographers = reservedPhotographers;
    }
}

