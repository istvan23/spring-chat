package com.example.springchatserver.service.room;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.Room;
import com.example.springchatserver.dto.RoomDto;
import com.example.springchatserver.mapper.RoomMapper;
import com.example.springchatserver.repository.GroupRepository;
import com.example.springchatserver.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final RoomMapper roomMapper;
    @Override
    public RoomDto getRoomById(Long id) {
        return this.roomMapper.convertToDto(this.roomRepository.findById(id).get());
    }

    @Override
    public List<RoomDto> getAlRoomByGroupId(Long groupId) {
        return this.groupRepository.findById(groupId).get().getRooms().stream()
                .map(room -> this.roomMapper.convertToDto(room)).collect(Collectors.toList());
    }

    @Override
    public RoomDto createRoom(RoomDto newRoom) {
        ChatGroup group = this.groupRepository.findById(newRoom.getChatGroupId()).get();
        Room room = new Room();
        room.setRoomName(newRoom.getRoomName());
        room.setChatGroup(group);
        LocalDateTime dateTimeOfCreation = LocalDateTime.now();
        room.setDateOfCreation(dateTimeOfCreation);
        group.getRooms().add(room);

        room = this.roomRepository.save(room);
        return this.roomMapper.convertToDto(room);
    }

    @Override
    public RoomDto updateRoom(Long id, RoomDto room) {
        Room updatedRoom = this.roomRepository.findById(id).get();
        updatedRoom.setRoomName(room.getRoomName());
        updatedRoom = this.roomRepository.save(updatedRoom);
        return this.roomMapper.convertToDto(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {
        this.roomRepository.deleteById(roomId);
    }
}