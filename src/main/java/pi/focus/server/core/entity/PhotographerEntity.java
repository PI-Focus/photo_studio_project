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
@Table(name = "photographers")
public class PhotographerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 32)
    String name;

    @Column(name = "surname", nullable = false, length = 32)
    String surname;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "photo_path", nullable = false, length = 64)
    String photoPath;

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL)
    private List<ReservedPhotographerEntity> reservedPhotographers = new ArrayList<>();

    public PhotographerEntity() {
    }

    public PhotographerEntity(
            UUID id,
            String name,
            String surname,
            String description,
            Integer price,
            String photoPath,
            List<ReservedPhotographerEntity> reservedPhotographers
    ) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.price = price;
        this.photoPath = photoPath;
        this.reservedPhotographers = reservedPhotographers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public List<ReservedPhotographerEntity> getReservedPhotographers() {
        return reservedPhotographers;
    }

    public void setReservedPhotographers(List<ReservedPhotographerEntity> reservedPhotographers) {
        this.reservedPhotographers = reservedPhotographers;
    }
}
