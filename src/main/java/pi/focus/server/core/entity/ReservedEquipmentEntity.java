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
 * Сущность для связи забронированного оборудования с конкретным заказом.
 * Позволяет хранить информацию о том, какая техника была выбрана 
 * и в каком количестве в рамках конкретного бронирования.
 */
@Entity
@Table(name = "reserved_equipment")
public class ReservedEquipmentEntity {
    /** Уникальный идентификатор записи о резервировании оборудования */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reserved_equipment_id", nullable = false, updatable = false)
    UUID reservedEquipmentId;

    /** Ссылка на объект основного бронирования, к которому привязана техника */
    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    ReservationEntity reservation;

    /** Ссылка на объект конкретного оборудования  из каталога */
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    EquipmentEntity equipment;

    /** 
     * Конструктор для спецификации для JPA
     */
    public ReservedEquipmentEntity() {
    }

    /** 
     * Конструктор для создания новой связи между заказом и оборудованием. 
     * @param reservedEquipmentId уникальный ID связи
     * @param reservation объект бронирования
     * @param equipment объект оборудования
     */
    public ReservedEquipmentEntity(UUID reservedEquipmentId, ReservationEntity reservation, EquipmentEntity equipment) {
        this.reservedEquipmentId = reservedEquipmentId;
        this.reservation = reservation;
        this.equipment = equipment;
    }

    public UUID getReservedEquipmentId() {
        return reservedEquipmentId;
    }

    public void setReservedEquipmentId(UUID reservedEquipmentId) {
        this.reservedEquipmentId = reservedEquipmentId;
    }

    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }

    public EquipmentEntity getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentEntity equipment) {
        this.equipment = equipment;
    }
}
