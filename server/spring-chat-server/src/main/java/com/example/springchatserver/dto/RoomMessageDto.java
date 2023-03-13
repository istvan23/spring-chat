package com.example.springchatserver.dto;

import com.example.springchatserver.domain.Room;
import com.example.springchatserver.domain.User;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class RoomMessageDto {
    private Long id;
    private Long groupId;
    private Long roomId;
    private Long senderId;
    private String senderUsername;
    private String message;
    private LocalDateTime dateOfMessage;
}
