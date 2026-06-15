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

@Entity
@Table(name = "reserved_equipment")
public class ReservedEquipmentEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reserved_equipment_id", nullable = false, updatable = false)
    UUID reservedEquipmentId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    EquipmentEntity equipment;

    @Column(name = "count", nullable = false)
    Integer count;

    public ReservedEquipmentEntity() {
    }

    public ReservedEquipmentEntity(UUID reservedEquipmentId, ReservationEntity reservation, EquipmentEntity equipment, Integer count) {
        this.reservedEquipmentId = reservedEquipmentId;
        this.reservation = reservation;
        this.equipment = equipment;
        this.count = count;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
