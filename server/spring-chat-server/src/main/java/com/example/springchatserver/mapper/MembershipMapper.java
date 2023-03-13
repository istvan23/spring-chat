package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.ChatGroupMembership;
import com.example.springchatserver.dto.MembershipDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class, ChatGroupMapper.class}
)
public interface MembershipMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "username", source = "user.username")
    })
    MembershipDto convertToDto(ChatGroupMembership chatGroupMembership);
}
