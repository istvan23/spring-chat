package com.example.springchatserver.service.room;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.RoomMessageDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomMessageService {
    RoomMessageDto getRoomMessageById(Long id);
    List<RoomMessageDto> getAllMessageByRoomId(Long roomId);
    List<RoomMessageDto> getMessagesByRoomIdAndTimeInterval(Long roomId, LocalDateTime from, LocalDateTime to);

    RoomMessageDto saveNewMessage(User sender, RoomMessageDto message);
    RoomMessageDto updateMessage(Long messageId, RoomMessageDto message);
    void deleteMessage(Long messageId);
}
