package pi.focus.server.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pi.focus.server.core.domain.Room;
import pi.focus.server.core.entity.RoomEntity;
import pi.focus.server.core.repository.RoomRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    @DisplayName("Должен вернуть список комнат, когда в БД есть данные")
    void shouldReturnAllRoomsWhenDataExists() {
        UUID room1Id = UUID.randomUUID();
        UUID room2Id = UUID.randomUUID();

        RoomEntity entity1 = new RoomEntity(room1Id, "Room 1", "Description 1");
        RoomEntity entity2 = new RoomEntity(room2Id, "Room 2", "Description 2");

        when(roomRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<Room> result = roomService.getAllRooms();

        assertThat(result).hasSize(2);

        assertThat(result.getFirst().id()).isEqualTo(room1Id);
        assertThat(result.getFirst().title()).isEqualTo("Room 1");
        assertThat(result.getFirst().description()).isEqualTo("Description 1");

        assertThat(result.get(1).id()).isEqualTo(room2Id);
        assertThat(result.get(1).title()).isEqualTo("Room 2");
        assertThat(result.get(1).description()).isEqualTo("Description 2");
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Должен вернуть пустой список, когда в БД нет комнат")
    void shouldReturnEmptyListWhenNoRooms() {
        when(roomRepository.findAll()).thenReturn(List.of());
        List<Room> result = roomService.getAllRooms();
        assertThat(result).isEmpty();
        verify(roomRepository).findAll();
    }

}