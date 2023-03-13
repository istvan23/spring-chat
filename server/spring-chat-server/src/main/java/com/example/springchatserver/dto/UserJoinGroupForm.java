package com.example.springchatserver.dto;

import lombok.Data;

@Data
public class UserJoinGroupForm {
    private final Long userId;
    private final Long groupId;
}
