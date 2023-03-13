package com.example.springchatserver.dto;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.RoomMessage;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomDto {
    private Long id;
    private String roomName;
    private Long chatGroupId;
    private List<RoomMessageDto> roomMessages;
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfLastMessage;
}
