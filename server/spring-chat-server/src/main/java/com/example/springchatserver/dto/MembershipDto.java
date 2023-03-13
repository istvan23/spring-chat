package com.example.springchatserver.dto;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.ChatGroupRole;
import com.example.springchatserver.domain.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class MembershipDto {
    private Long id;
    private Long userId;
    private String username;
    private ChatGroupDto chatGroup;
    private RoleDto role;
    private LocalDateTime dateOfJoin;
}
