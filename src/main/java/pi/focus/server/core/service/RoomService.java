package pi.focus.server.core.service;

import io.hypersistence.utils.hibernate.type.range.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pi.focus.server.api.context.IConcretePhotoroomContext;
import pi.focus.server.api.context.IPhotoroomsContext;
import pi.focus.server.api.models.ICalendar;
import pi.focus.server.api.models.IDataCard;
import pi.focus.server.core.entity.PhotoEntity;
import pi.focus.server.core.entity.ReservationEntity;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.repository.RoomRepository;
import pi.focus.server.core.service.api.IRoomService;
import pi.focus.server.service.context.CalendarDto;
import pi.focus.server.service.context.ConcretePhotoroomContextDto;
import pi.focus.server.service.context.PhotoroomsContextDto;
import pi.focus.server.service.models.DataCardDto;
import pi.focus.server.service.models.TextCardDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Profile({"dev", "prod", "test"})
public class RoomService implements IRoomService {
    @Value("${app.static-data.placeholder-path}")
    private String placeholderPath;
    @Value("${app.timezone}")
    private String timezone;
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public IPhotoroomsContext getPhotoroomsContext() {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        List<IDataCard> dataCards = new ArrayList<>();
        String photoPath = placeholderPath;
        for (RoomEntity roomEntity: roomEntities) {
            List<PhotoEntity> photos = roomEntity.getPhotos();
            if (!photos.isEmpty()) {
                photoPath = photos.getFirst().getPath();
            }
            dataCards.add(
                    new DataCardDto(
                            roomEntity.getTitle(),
                            roomEntity.getDescription(),
                            photoPath,
                            "photorooms/" + roomEntity.getId()
                    )
            );
        }
        return new PhotoroomsContextDto(dataCards);
    }

    @Override
    public IConcretePhotoroomContext getConcretePhotoroomContext(UUID id) {
        Optional<RoomEntity> roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            return null;
        }
        RoomEntity roomEntity = roomOpt.get();
        return new ConcretePhotoroomContextDto(
                new TextCardDto(
                        "Зал " + roomEntity.getTitle(),
                        roomEntity.getDescription()
                ),
                roomEntity.getPhotos().stream().map(PhotoEntity::getPath).toList(),
                id
        );
    }

    @Override
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public ICalendar getRoomCalendar(UUID id, LocalDate day) {
        Optional<RoomEntity> roomOpt = roomRepository.findById(id);
        if (roomOpt.isEmpty()) {
            return null;
        }
        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.of(timezone));
        LocalDate nowDay = zonedNow.toLocalDate();
        LocalTime nowTime = zonedNow.toLocalTime();
        RoomEntity roomEntity = roomOpt.get();
        int price = roomEntity.getPrice();
        List<List<Integer>> calendar = new ArrayList<>();
        LocalDate monday = day.with(DayOfWeek.MONDAY);
        boolean isNextWeek = false;
        if (monday.isBefore(nowDay.with(DayOfWeek.MONDAY))) {
            return null;
        } else if (monday.isAfter(nowDay.with(DayOfWeek.MONDAY))) {
            isNextWeek = true;
        }
        for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
            calendar.add(new ArrayList<>(Collections.nCopies(14, price)));
            LocalDate currentDay = monday.plusDays(dayIndex);
            for (ReservationEntity reservation: roomEntity.getReservations()) {
                Range<LocalDateTime> interval = reservation.getTime();
                LocalDateTime start = interval.lower();
                LocalDateTime end = interval.upper();
                if (start.toLocalDate().equals(currentDay) && (nowDay.isAfter(currentDay) || isNextWeek)) {
                    int startHour = start.getHour();
                    int endHour = end.getHour();
                    for (int hour = startHour; hour < endHour; hour++) {
                        if (nowTime.getHour() < hour || isNextWeek) {
                            calendar.get(dayIndex).set(hour - 8, -1);
                        }
                    }
                }
            }
        }
        return new CalendarDto(calendar);
    }
}
