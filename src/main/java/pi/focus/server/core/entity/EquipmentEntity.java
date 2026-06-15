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

/**
 * Сущность оборудования для хранения в базе данных.
 * Соответствует таблице "equipment" и содержит информацию о технике, доступной для аренды в студии.
 */
@Entity
@Table(name = "equipment")
public class EquipmentEntity {
    /** Уникальный идентификатор оборудования в базе данных */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Название оборудования */
    @Column(name = "title", nullable = false, length = 32)
    String title;

    /** Описание оборудования */
    @Column(name = "description", nullable = false)
    String description;

    /** Стоимость аренды*/
    @Column(name = "price", nullable = false)
    private Integer price;

    /** Относительный путь к файлу изображения оборудования */
    @Column(name = "photo_path", nullable = false, length = 64)
    String photoPath;

    /** Список всех записей о бронировании, в которых участвует данное оборудование */
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<ReservedEquipmentEntity> reservedEquipments = new ArrayList<>();

    /**
     * Коструктор. Нужен для работы спецификации JPA
     */
    public EquipmentEntity() {
    }

    /**
     * Конструктор для инициализации сущности со всеми параметрами.
     * @param id уникальный идентификатор
     * @param title название оборудования
     * @param description описание оборудования
     * @param price стоимость аренды
     * @param photoPath путь к фотографии
     * @param reservedEquipments список связанных бронирований
     */
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
