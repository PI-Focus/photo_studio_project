package pi.focus.server.core.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "equipment")
public class EquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 32)
    String title;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "photo_path", nullable = false, length = 64)
    String photoPath;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<ReservedEquipmentEntity> reservedEquipments = new ArrayList<>();

    public EquipmentEntity() {
    }

    public EquipmentEntity(
            UUID id,
            String title,
            String description,
            Integer price,
            String photoPath,
            List<ReservedEquipmentEntity> reservedEquipments
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.photoPath = photoPath;
        this.reservedEquipments = reservedEquipments;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public List<ReservedEquipmentEntity> getReservedEquipments() {
        return reservedEquipments;
    }

    public void setReservedEquipments(List<ReservedEquipmentEntity> reservedEquipments) {
        this.reservedEquipments = reservedEquipments;
    }
}
