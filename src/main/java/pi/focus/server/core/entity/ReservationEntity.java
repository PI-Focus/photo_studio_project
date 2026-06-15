package pi.focus.server.core.entity;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
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
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private PhotographerEntity photographer;

    @Column(name = "time", nullable = false, columnDefinition = "tsrange")
    @Type(PostgreSQLRangeType.class)
    private Range<LocalDateTime> time;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservedEquipmentEntity> reservedEquipments = new ArrayList<>();

    public ReservationEntity() {
    }

    public ReservationEntity(UUID id, UserEntity user, RoomEntity room, PhotographerEntity photographer, Range<LocalDateTime> time, List<ReservedEquipmentEntity> reservedEquipments) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.photographer = photographer;
        this.time = time;
        this.reservedEquipments = reservedEquipments;
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

    public PhotographerEntity getPhotographer() {
        return photographer;
    }

    public void setPhotographer(PhotographerEntity photographer) {
        this.photographer = photographer;
    }

    public Range<LocalDateTime> getTime() {
        return time;
    }

    public void setTime(Range<LocalDateTime> time) {
        this.time = time;
    }

    public List<ReservedEquipmentEntity> getReservedEquipments() {
        return reservedEquipments;
    }

    public void setReservedEquipments(List<ReservedEquipmentEntity> reservedEquipments) {
        this.reservedEquipments = reservedEquipments;
    }
}

