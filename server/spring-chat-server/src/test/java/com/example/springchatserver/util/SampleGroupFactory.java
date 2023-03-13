package com.example.springchatserver.util;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.dto.ChatGroupDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleGroupFactory {

    public static ChatGroup createSimpleChatGroup(String name, User creator) {
        ChatGroup group = new ChatGroup();
        LocalDateTime dateTime = LocalDateTime.now();
        group.setName(name);
        group.setGroupAdministrator(creator);
        ChatGroupMembership chatGroupMembership = new ChatGroupMembership();
        chatGroupMembership.setChatGroup(group);
        chatGroupMembership.setUser(creator);
        chatGroupMembership.setDateOfJoin(LocalDateTime.now());
        ChatGroupRole chatGroupRole = new ChatGroupRole();
        chatGroupRole.setName("ROLE_FOUNDER");
        chatGroupRole.setDisplayName("Founder");
        chatGroupRole.setUserMembership(chatGroupMembership);

        ChatGroupPrivilege readPrivilege = new ChatGroupPrivilege();
        readPrivilege.setName("READ_GROUP");
        readPrivilege.setRole(chatGroupRole);

        ChatGroupPrivilege updatePrivilege = new ChatGroupPrivilege();
        updatePrivilege.setRole(chatGroupRole);
        updatePrivilege.setName("UPDATE_GROUP");

        ChatGroupPrivilege deletePrivilege = new ChatGroupPrivilege();
        deletePrivilege.setName("DELETE_GROUP");
        deletePrivilege.setRole(chatGroupRole);

        chatGroupRole.setAuthorities(List.of(readPrivilege, updatePrivilege, deletePrivilege));
        chatGroupMembership.setRole(chatGroupRole);
        group.setMembers(List.of(chatGroupMembership));
        group.setRooms(new ArrayList<>());
        group.setDateOfCreation(LocalDateTime.now());
        creator.setChatGroupMemberships(List.of(chatGroupMembership));
        return group;
    }
    public static ChatGroupDto createSimpleChatGroupDto(String name, User creator) {
        ChatGroupDto chatGroupDto = new ChatGroupDto();
        chatGroupDto.setName(name);
        chatGroupDto.setDateOfCreation(LocalDateTime.now());
        chatGroupDto.setGroupAdministratorId(creator.getId());
        chatGroupDto.setGroupAdministratorUsername(creator.getUsername());
        chatGroupDto.setRooms(new ArrayList<>());
        chatGroupDto.setGroupAnnouncements(new ArrayList<>());
        return chatGroupDto;
    }

    public static GroupAnnouncement createSimpleAnnouncement(String title, String text, User author, ChatGroup group){
        GroupAnnouncement groupAnnouncement = new GroupAnnouncement();
        groupAnnouncement.setAuthor(author);

        LocalDateTime currentDate = LocalDateTime.now();

        groupAnnouncement.setDateOfCreation(currentDate);
        groupAnnouncement.setText(text);
        groupAnnouncement.setTitle(title);
        groupAnnouncement.setChatGroup(group);
        groupAnnouncement.setDateOfLastModification(currentDate);
        return groupAnnouncement;
    }
}
