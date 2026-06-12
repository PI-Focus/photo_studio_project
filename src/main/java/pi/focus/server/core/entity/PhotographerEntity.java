package pi.focus.server.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
    @Column(name = "photo_path", nullable = false, length = 64)
    String photoPath;

    public PhotographerEntity() {
    }

    public PhotographerEntity(
        UUID id,
        String name,
        String surname,
        String description,
        String photoPath
    ) {
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.photoPath = photoPath;
    }

    public UUID getId() {
        return id;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
