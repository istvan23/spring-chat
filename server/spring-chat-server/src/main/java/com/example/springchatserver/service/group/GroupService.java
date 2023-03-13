package com.example.springchatserver.service.group;

import com.example.springchatserver.dto.BasicChatGroupInformationDto;
import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.GroupAnnouncementDto;
import com.example.springchatserver.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupService {
    ChatGroupDto createNewChatGroup(ChatGroupDto newChatGroup);
    ChatGroupDto updateChatGroup(Long id, ChatGroupDto chatGroupDto);
    void deleteChatGroup(Long id);
    ChatGroupDto getChatGroupById(Long id);
    List<ChatGroupDto> getAllChatGroup();
    List<BasicChatGroupInformationDto> getBasicInformationAboutChatGroups(String filterText);
    //List<UserDto> getMemberOfChatGroupById(Long groupId);

    /*
    GroupAnnouncementDto getAnnouncementById(Long announcementId);
    List<GroupAnnouncementDto> getAnnouncementsOfGroupByTimeInterval(Long groupId, LocalDateTime from, LocalDateTime to);
    GroupAnnouncementDto addNewAnnouncementToGroup(Long groupId, GroupAnnouncementDto announcement);
    GroupAnnouncementDto updateAnnouncement(Long announcementId, GroupAnnouncementDto announcement);
    void deleteAnnouncement(Long announcementId);

     */
}
