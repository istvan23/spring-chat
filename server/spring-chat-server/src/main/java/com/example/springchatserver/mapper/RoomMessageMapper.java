package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.RoomMessage;
import com.example.springchatserver.dto.RoomMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring"
)
public interface RoomMessageMapper {
    @Mappings({
            @Mapping(target = "groupId", source = "room.chatGroup.id"),
            @Mapping(target = "roomId", source = "room.id"),
            @Mapping(target = "senderId", source = "sender.id"),
            @Mapping(target = "senderUsername", source = "sender.username")
    })
    RoomMessageDto convertToDto(RoomMessage roomMessage);
}
