package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.dto.BasicChatGroupInformationDto;
import com.example.springchatserver.dto.ChatGroupDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {RoomMapper.class, GroupAnnouncementMapper.class}
)
public interface ChatGroupMapper {
    @Mappings({
            @Mapping(target = "groupAdministratorId", source = "groupAdministrator.id"),
            @Mapping(target = "groupAdministratorUsername", source = "groupAdministrator.username")
    })
    ChatGroupDto convertToDto(ChatGroup chatGroup);
    BasicChatGroupInformationDto convertToShallowDto(ChatGroup chatGroup);
}
