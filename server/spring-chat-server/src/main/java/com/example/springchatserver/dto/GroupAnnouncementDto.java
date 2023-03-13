package com.example.springchatserver.dto;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupAnnouncementDto {
    private Long id;
    private String title;
    private String text;
    private Long authorId;
    private String authorUsername;
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfLastModification;
    private Long chatGroupId;
}
