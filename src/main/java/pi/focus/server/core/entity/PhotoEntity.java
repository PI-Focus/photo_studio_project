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

/**
 * Сущность фотографии интерьера зала.
 * Используется для хранения путей к изображениям, которые отображаются в галерее конкретного фотозала.
 */
@Entity
@Table(name = "photos")
public class PhotoEntity {
    /** Уникальный идентификатор записи о фотографии */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Объект фотозала, к которому относится данная фотография */
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    /** Относительный путь к файлу изображения на сервере или в хранилище */
    @Column(name = "path", nullable = false, length = 64)
    private String path;

    /** 
     * Конструктор для спецификации JPA
     */
    public PhotoEntity() {
    }

    /** 
     * Конструктор для создания новой сущности фотографии
     * @param id уникальный идентификатор
     * @param room объект зала, к которому привязывается фото
     * @param path путь к файлу изображения
     */
    public PhotoEntity(UUID id, RoomEntity room, String path) {
        this.id = id;
        this.room = room;
        this.path = path;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
