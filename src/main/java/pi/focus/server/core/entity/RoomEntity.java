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
 * Сущность фотозала студии.
 * Описывает конкретную локацию для съемок, её характеристики, стоимость 
 * и содержит ссылки на галерею изображений и историю бронирований.
 */
@Entity
@Table(name = "rooms")
public class RoomEntity {
    /** Уникальный идентификатор зала */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Название зала */
    @Column(name = "title", nullable = false, length = 32)
    private String title;

    /** Описание интерьера и возможностей зала  */
    @Column(name = "description", nullable = false)
    private String description;

    /** Базовая стоимость аренды зала за один час */
    @Column(name = "price", nullable = false)
    private Integer price;

    /** Список фотографий интерьера, используемых для отображения галереи зала на сайте */
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<PhotoEntity> photos = new ArrayList<>();

    /** Список всех записей о бронировании, относящихся к данному залу */
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations = new ArrayList<>();

    /** Конструктор для спецификации JPA */
    public RoomEntity() {
    }

    /**
     * Конструктор для создания полной записи о фотозале.
     * 
     * @param id уникальный идентификатор
     * @param title название зала
     * @param description описание интерьера
     * @param price стоимость аренды
     * @param photos список фотографий галереи
     * @param reservations список связанных бронирований
     */
    public RoomEntity(
            UUID id,
            String title,
            String description,
            Integer price,
            List<PhotoEntity> photos,
            List<ReservationEntity> reservations
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.photos = photos;
        this.reservations = reservations;
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

    public List<PhotoEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoEntity> photos) {
        this.photos = photos;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
}
