package com.example.springchatserver.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BasicChatGroupInformationDto {
    private Long id;
    private String name;
    private LocalDateTime dateOfCreation;
    private Long groupAdministratorId;
    private String groupAdministratorUsername;
}
