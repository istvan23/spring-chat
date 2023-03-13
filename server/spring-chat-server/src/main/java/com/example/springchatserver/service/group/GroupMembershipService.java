package com.example.springchatserver.service.group;

import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.MembershipDto;

import java.util.List;

public interface GroupMembershipService {
    //List<ChatGroupDto> getChatGroupsByUserMembership(Long userId);
    MembershipDto getChatGroupMembershipById(Long membershipId);
    List<MembershipDto> getMembersOfChatGroup(Long groupId);
    MembershipDto addNewMemberToGroup(Long groupId, Long userId) throws Exception;
    ChatGroupDto removeMemberFromGroup(Long groupId, Long userId) throws Exception;
    MembershipDto updateMembership(Long id, MembershipDto updatableMembership) throws Exception;
    List<MembershipDto> getGroupMembershipsOfUser(Long userId);
}
