package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.Room;
import com.example.springchatserver.dto.RoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {RoomMessageMapper.class}
)
public interface RoomMapper {
    @Mapping(target = "chatGroupId", source = "chatGroup.id")
    RoomDto convertToDto(Room room);
}
