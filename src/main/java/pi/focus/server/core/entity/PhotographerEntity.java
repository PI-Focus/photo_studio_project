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
 * Сущность фотографа для хранения в базе данных.
 * Соответствует таблице фотографов и содержит информацию о сотрудниках студии,
 * чьи услуги можно забронировать.
 */
@Entity
@Table(name = "photographers")
public class PhotographerEntity {
    /** Уникальный идентификатор фотографа в системе */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Имя фотографа  */
    @Column(name = "name", nullable = false, length = 32)
    String name;

    /** Фамилия фотографа */
    @Column(name = "surname", nullable = false, length = 32)
    String surname;

    /** Описание специализации, опыта или творческого стиля фотографа */
    @Column(name = "description", nullable = false)
    String description;

    /** Стоимость услуг фотографа за час работы */
    @Column(name = "price", nullable = false)
    private Integer price;

    /** Путь к портретной фотографии фотографа */
    @Column(name = "photo_path", nullable = false, length = 64)
    String photoPath;

    /** Список всех записей о бронировании, в которых задействован данный фотограф */
    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL)
    private List<ReservedPhotographerEntity> reservedPhotographers = new ArrayList<>();

    /** Конструктор для спецификации JPA */
    public PhotographerEntity() {
    }

    /**
     * Конструктор для полной инициализации сущности фотографа
     * @param id уникальный идентификатор
     * @param name имя
     * @param surname фамилия
     * @param description описание
     * @param price почасовая ставка
     * @param photoPath путь к портретному фото
     * @param reservedPhotographers список связанных бронирований
     */
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
