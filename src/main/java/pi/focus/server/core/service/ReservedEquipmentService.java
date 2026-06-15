package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.service.api.IReservedEquipmentService;

/**
 * Сервис для управления дополнительным оборудованием, привязанным к бронированиям.
 */
@Service
@Profile({"dev", "prod", "test"})
public class ReservedEquipmentService implements IReservedEquipmentService {
}
