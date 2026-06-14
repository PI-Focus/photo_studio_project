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
@Table(name = "reserved_photographers")
public class ReservedPhotographerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reserved_photographer_id", nullable = false, updatable = false)
    private UUID reservedPhotographerId;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private PhotographerEntity photographer;

    public ReservedPhotographerEntity() {
    }

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
