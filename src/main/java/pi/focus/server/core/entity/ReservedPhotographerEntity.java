package pi.focus.server.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

/** 
 * Сущность для связи забронированного фотографа с конкретным заказом.
 * Позволяет закрепить услуги одного или нескольких фотографов за определенным бронированием зала.
 */
@Entity
@Table(name = "reserved_photographers")
public class ReservedPhotographerEntity {
    /** Уникальный идентификатор записи о резервированнии фотографа */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reserved_photographer_id", nullable = false, updatable = false)
    private UUID reservedPhotographerId;

    /** Ссылка на объект основного бронирования, к которому привязан фотограф */
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;

    /** Ссылка на выбранного фотографа из команды студии */
    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private PhotographerEntity photographer;

    /** Конструктор для спецификации  JPA */
    public ReservedPhotographerEntity() {
    }

    /**
     * Конструктор для создания новой связи между заказом и фотографом
     * @param reservedPhotographerId уникальный ID связи
     * @param reservation объект бронирования
     * @param photographer объект фотографа
     */
    public ReservedPhotographerEntity(UUID reservedPhotographerId, ReservationEntity reservation, PhotographerEntity photographer) {
        this.reservedPhotographerId = reservedPhotographerId;
        this.reservation = reservation;
        this.photographer = photographer;
    }

    public UUID getReservedPhotographerId() {
        return reservedPhotographerId;
    }

    public void setReservedPhotographerId(UUID reservedPhotographerId) {
        this.reservedPhotographerId = reservedPhotographerId;
    }

    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }

    public PhotographerEntity getPhotographer() {
        return photographer;
    }

    public void setPhotographer(PhotographerEntity photographer) {
        this.photographer = photographer;
    }
}
