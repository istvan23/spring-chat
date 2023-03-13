package com.example.springchatserver.dto;

import com.example.springchatserver.domain.GroupAnnouncement;
import com.example.springchatserver.domain.Room;
import com.example.springchatserver.domain.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ChatGroupDto {
    private Long id;
    private String name;
    private List<RoomDto> rooms;
    private List<GroupAnnouncementDto> groupAnnouncements;
    private LocalDateTime dateOfCreation;
    private Long groupAdministratorId;
    private String groupAdministratorUsername;
}
