package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.AbstractRole;
import com.example.springchatserver.domain.ChatGroupRole;
import com.example.springchatserver.domain.Role;
import com.example.springchatserver.dto.PrivilegeDto;
import com.example.springchatserver.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {PrivilegeMapper.class}
)
public interface RoleMapper {
    @Mapping(target = "userId", source = "user.id")
    RoleDto convertToDto(Role role);
    @Mapping(target = "userId", source = "userMembership.user.id")
    RoleDto convertToDto(ChatGroupRole role);
}
