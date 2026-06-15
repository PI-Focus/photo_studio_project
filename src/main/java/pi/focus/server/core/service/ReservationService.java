package pi.focus.server.core.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.core.service.api.IReservationService;

/**
 * Реализация сервиса для управления бронированиями залов.
 * В данной версии содержит базовую структуру для будущей реализации бизнес-логики заказов.
 */
@Service
@Profile({"dev", "prod", "test"})
public class ReservationService implements IReservationService {
}
