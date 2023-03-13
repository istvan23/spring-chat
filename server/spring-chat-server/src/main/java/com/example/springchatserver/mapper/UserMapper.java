package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {MembershipMapper.class, RoleMapper.class}
)
public interface UserMapper{
    UserDto convertToDto(User user);
}
