package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.models.IEquipment;
import pi.focus.server.api.models.IOrder;
import pi.focus.server.api.models.IOrderStatus;
import pi.focus.server.core.domain.Equipment;
import pi.focus.server.core.domain.Photographer;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.service.api.IEquipmentService;
import pi.focus.server.core.service.api.IOrderService;
import pi.focus.server.core.service.api.IPhotographerService;
import pi.focus.server.core.service.api.IReservationService;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.models.OrderDto;
import pi.focus.server.service.models.OrderStatusDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Profile({"dev", "prod", "test"})
@SuppressWarnings({"PMD.ConfusingTernary"})
public class OrderService implements IOrderService {
    private final IRoomService roomService;
    private final IEquipmentService equipmentService;
    private final IPhotographerService photographerService;
    private final IReservationService reservationService;

    public OrderService(
            IRoomService roomService,
            IEquipmentService equipmentService,
            IPhotographerService photographerService,
            IReservationService reservationService
    ) {
        this.roomService = roomService;
        this.equipmentService = equipmentService;
        this.photographerService = photographerService;
        this.reservationService = reservationService;
    }

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

    @Override
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public Integer validateOrderStatus(IOrderStatus orderStatus) {
        int price = 0;
        if (orderStatus.getRoomId() == null) {
            return -1;
        } else {
            Room room = roomService.getRoomById(orderStatus.getRoomId());
            if (room == null) {
                return -1;
            } else {
                price += room.price();
            }
        }
        IOrder order = orderStatus.getBody();
        if (order.getStartTime() == null || order.getEndTime() == null
                || !order.getStartTime().isBefore(order.getEndTime())
                || !order.getStartTime().toLocalDate().isEqual(order.getEndTime().toLocalDate())) {
            return -1;
        }
        boolean changed = false;
        if (order.getPhotographerId() != null) {
            if (!photographerService.freePhotographer(order.getPhotographerId(),
                    Range.closed(order.getStartTime(), order.getEndTime()))) {
                changed = true;
                order.setPhotographerId(null);
            } else {
                Photographer photographer = photographerService.getPhotographerById(order.getPhotographerId());
                price += photographer.price();
            }
        }
        List<IEquipment> validEquipment = new ArrayList<>();
        for (IEquipment equipmentDto: order.getEquipment()) {
            if (equipmentDto.getId() != null) {
                Equipment equipment = equipmentService.getEquipmentById(equipmentDto.getId());
                if (equipment != null && equipmentDto.getCount() != null && equipmentDto.getCount() > 0) {
                    validEquipment.add(equipmentDto);
                    price += equipment.price() * equipmentDto.getCount();
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
