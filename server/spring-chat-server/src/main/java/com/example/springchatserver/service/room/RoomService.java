package com.example.springchatserver.service.room;

import com.example.springchatserver.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto getRoomById(Long id);
    List<RoomDto> getAlRoomByGroupId(Long groupId);
    RoomDto createRoom(RoomDto newRoom);
    RoomDto updateRoom(Long id, RoomDto room);
    void deleteRoom(Long roomId);
}
