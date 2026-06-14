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

@Entity
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    RoomEntity room;

    @Column(name = "day", nullable = false, columnDefinition = "DATE")
    LocalDate day;

    @Column(name = "from_time", nullable = false, columnDefinition = "SMALLINT")
    Short fromTime;

    @Column(name = "to_time", nullable = false, columnDefinition = "SMALLINT")
    Short toTime;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservedEquipmentEntity> reservedEquipments = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservedPhotographerEntity> reservedPhotographers = new ArrayList<>();

    public ReservationEntity() {
    }

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

