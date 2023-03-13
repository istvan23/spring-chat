package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.GroupAnnouncement;
import com.example.springchatserver.dto.GroupAnnouncementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring"
)
public interface GroupAnnouncementMapper {
    @Mappings({
            @Mapping(target = "authorId", source = "author.id"),
            @Mapping(target = "authorUsername", source = "author.username"),

            @Mapping(target = "chatGroupId", source = "chatGroup.id")
    })
    GroupAnnouncementDto convertToDto(GroupAnnouncement groupAnnouncement);
}
