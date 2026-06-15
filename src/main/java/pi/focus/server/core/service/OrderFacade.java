package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.models.IEquipment;
import pi.focus.server.api.models.IOrder;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.entity.EquipmentEntity;
import pi.focus.server.core.entity.PhotographerEntity;
import pi.focus.server.core.entity.ReservationEntity;
import pi.focus.server.core.entity.ReservedEquipmentEntity;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.repository.EquipmentRepository;
import pi.focus.server.core.repository.PhotographerRepository;
import pi.focus.server.core.repository.ReservationRepository;
import pi.focus.server.core.repository.RoomRepository;
import pi.focus.server.core.repository.UserRepository;
import pi.focus.server.core.service.api.IOrderFacade;
import pi.focus.server.service.models.OrderDto;
import pi.focus.server.service.models.OrderStatusDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Основная реализация фасада управления заказами.
 * Координирует работу сервисов залов и фотографов, а также напрямую взаимодействует
 * с репозиториями для проверки доступности ресурсов, расчета стоимости и сохранения бронирований.
 */
@Service
@Transactional
@Profile({"dev", "prod", "test"})
@Primary
@SuppressWarnings({"PMD.ConfusingTernary"})
public class OrderFacade implements IOrderFacade {
    private final RoomService roomService;
    private final PhotographerService photographerService;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final EquipmentRepository equipmentRepository;
    private final PhotographerRepository photographerRepository;
    private final UserRepository userRepository;

    /** Конструктор со всеми необходимыми зависимостями для работы с заказами */
    public OrderFacade(
            RoomService roomService,
            PhotographerService photographerService,
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            EquipmentRepository equipmentRepository,
            PhotographerRepository photographerRepository, UserRepository userRepository
    ) {
        this.roomService = roomService;
        this.photographerService = photographerService;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.equipmentRepository = equipmentRepository;
        this.photographerRepository = photographerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Создает начальный объект заказа с пустыми полями и нулевой ценой.
     * @return инициализированный DTO статуса заказа
     */
    @Override
    public IOrderStatus getEmptyOrderStatus() {
        return new OrderStatusDto(
                null,
                new OrderDto(
                        null,
                        null,
                        null,
                        new ArrayList<>(),
                        0
                )
        );
    }

    /**
     * Сохраняет данные о бронировании в базу данных.
     * Преобразует данные из DTO в сущности и связывает их с пользователем,
     * залом, фотографом и списком забронированного оборудования.
     * 
     * @param id уникальный идентификатор пользователя
     * @param orderStatus объект заказа для сохранения
     */
    @Override
    public void createReservation(UUID id, IOrderStatus orderStatus) {
        IOrder order = orderStatus.getBody();

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(null);
        reservation.setUser(userRepository.findById(id).orElse(null));
        reservation.setRoom(roomRepository.findById(orderStatus.getRoomId()).orElse(null));
        reservation.setPhotographer(photographerRepository.findById(order.getPhotographerId()).orElse(null));

        Range<LocalDateTime> time = Range.closed(
                order.getStartTime(),
                order.getEndTime()
        );
        reservation.setTime(time);

        List<ReservedEquipmentEntity> reservedEquipmentEntities = new ArrayList<>();
        for (IEquipment equipment: order.getEquipment()) {
            reservedEquipmentEntities.add(
                    new ReservedEquipmentEntity(
                            null,
                            reservation,
                            equipmentRepository.findById(equipment.getId()).orElse(null),
                            equipment.getCount()
                    )
            );
        }
        reservation.setReservedEquipments(reservedEquipmentEntities);

        reservationRepository.save(reservation);
    }

    /**
     * Выполняет валидацию заказа и расчет итоговой стоимости.
     * Проверки включают:
     * - Существование выбранного зала.
     * - Корректность временного интервала 
     * - Свободность зала на выбранное время.
     * - Свободность фотографа.
     * - Доступность каждой единицы оборудования.
     * 
     * Если фотограф или оборудование недоступны, они удаляются из заказа, а метод возвращает 1.
     * 
     * @param orderStatus данные заказа для проверки
     * @return 0 - заказ полностью валиден; 
     *         1 - заказ скорректирован;
     *        -1 - критическая ошибка валидации.
     */
    @Override
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public Integer validateOrderStatus(IOrderStatus orderStatus) {
        int price = 0;
        RoomEntity room;
        if (orderStatus.getRoomId() == null) {
            return -1;
        } else {
            room = roomRepository.findById(orderStatus.getRoomId()).orElse(null);
            if (room == null) {
                return -1;
            } else {
                price += room.getPrice();
            }
        }
        IOrder order = orderStatus.getBody();
        if (order.getStartTime() == null || order.getEndTime() == null
                || !order.getStartTime().isBefore(order.getEndTime())
                || !order.getStartTime().toLocalDate().isEqual(order.getEndTime().toLocalDate())) {
            return -1;
        }
        if (!roomService.freeRoom(room.getId(), Range.closed(order.getStartTime(), order.getEndTime()))) {
            return -1;
        }
        boolean changed = false;
        if (order.getPhotographerId() != null) {
            if (!photographerService.freePhotographer(order.getPhotographerId(),
                    Range.closed(order.getStartTime(), order.getEndTime()))) {
                changed = true;
                order.setPhotographerId(null);
            } else {
                PhotographerEntity photographer = photographerRepository.findById(order.getPhotographerId()).get();
                price += photographer.getPrice();
            }
        }
        List<IEquipment> validEquipment = new ArrayList<>();
        for (IEquipment equipmentDto: order.getEquipment()) {
            if (equipmentDto.getId() != null) {
                EquipmentEntity equipment = equipmentRepository.findById(equipmentDto.getId()).orElse(null);
                if (equipment != null && equipmentDto.getCount() != null && equipmentDto.getCount() > 0) {
                    validEquipment.add(equipmentDto);
                    price += equipment.getPrice() * equipmentDto.getCount();
                } else {
                    changed = true;
                }
            } else {
                changed = true;
            }
        }
        order.setEquipment(validEquipment);
        order.setPrice(price);
        if (changed) {
            return 1;
        }
        return 0;
    }
}
