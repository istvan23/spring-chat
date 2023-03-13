package com.example.springchatserver.dto;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.Role;
import lombok.Data;

import java.util.List;
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private List<RoleDto> roles;
    private List<MembershipDto> chatGroupMemberships;
}
