package com.example.springchatserver.dto;

import com.example.springchatserver.domain.ChatBasicAuthority;
import com.example.springchatserver.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private Long id;
    private Long userId;
    private String name;
    private String displayName;
    private List<PrivilegeDto> authorities;
}
