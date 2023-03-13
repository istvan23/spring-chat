package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.repository.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of GroupSecurityService
 */
@Service("groupSecurityService")
public class GroupSecurityServiceImpl implements GroupSecurityService{

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupAnnouncementRepository groupAnnouncementRepository;
    private final RoomRepository roomRepository;
    private final RoomMessageRepository roomMessageRepository;
    private final GroupRoleRepository groupRoleRepository;

    public GroupSecurityServiceImpl(GroupRepository groupRepository,
                                    GroupMembershipRepository groupMembershipRepository,
                                    GroupAnnouncementRepository groupAnnouncementRepository,
                                    RoomRepository roomRepository, RoomMessageRepository roomMessageRepository,
                                    GroupRoleRepository groupRoleRepository) {
        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupAnnouncementRepository = groupAnnouncementRepository;
        this.roomRepository = roomRepository;
        this.roomMessageRepository = roomMessageRepository;
        this.groupRoleRepository = groupRoleRepository;
    }

    private boolean determinationOfAuthorityOfUser(ChatGroupMembership membershipOfRequester, String actionType){
        if (membershipOfRequester.getRole() != null && !membershipOfRequester.getRole().getAuthorities().isEmpty()){
            List<ChatGroupPrivilege> authoritiesOfUserInTheGivenGroup = membershipOfRequester.getRole().getAuthorities();
            return authoritiesOfUserInTheGivenGroup
                    .stream()
                    .anyMatch(authority ->
                            authority.getName().equals(actionType)
                                    || authority.getName().equals("GROUP_ADMINISTRATOR"));
        }
        return false;
    }

    @Override
    public boolean checkUserAuthorityToAccessGroup(User user, Long groupId, String actionType) {
        ChatGroupMembership membership = user.getChatGroupMemberships()
                .stream()
                .filter(chatGroupMembership ->
                        chatGroupMembership.getChatGroup().getId().equals(groupId)).findFirst().orElseThrow();
        return determinationOfAuthorityOfUser(membership, actionType);
    }

    @Override
    public boolean checkUserAuthorityToAccessMembership(User user, Long membershipId, String actionType) {
        ChatGroupMembership membership = this.groupMembershipRepository.findById(membershipId).orElseThrow();
        ChatGroupMembership membershipOfRequester = user.getChatGroupMemberships()
                .stream()
                .filter(chatGroupMembership ->
                        chatGroupMembership.getChatGroup().getId().equals(membership.getChatGroup().getId())).findFirst().orElseThrow();
        return determinationOfAuthorityOfUser(membershipOfRequester, actionType);
    }

    @Override
    public boolean checkUserAuthorityToAccessAnnouncement(User user, Long announcementId, String actionType) {
        GroupAnnouncement groupAnnouncement = this.groupAnnouncementRepository.findById(announcementId).orElseThrow();
        ChatGroupMembership membership = user.getChatGroupMemberships()
                .stream()
                .filter(chatGroupMembership ->
                        chatGroupMembership
                                .getChatGroup()
                                .getId()
                                .equals(groupAnnouncement.getChatGroup().getId())).findFirst().orElseThrow();
        return determinationOfAuthorityOfUser(membership, actionType);
    }

    @Override
    public boolean checkUserAuthorityToAccessRoom(User user, Long roomId, String actionType) {
        Room room = this.roomRepository.findById(roomId).orElseThrow();
        ChatGroupMembership membership = user.getChatGroupMemberships()
                .stream()
                .filter(chatGroupMembership ->
                        chatGroupMembership
                                .getChatGroup()
                                .getId().equals(room.getChatGroup().getId())).findFirst().orElseThrow();
        return determinationOfAuthorityOfUser(membership, actionType);
    }

    @Override
    public boolean checkUserAuthorityToAccessMessage(User user, Long messageId, String actionType) {
        RoomMessage message = this.roomMessageRepository.findById(messageId).orElseThrow();

        if (message.getSender().equals(user)) return true;

        ChatGroupMembership membership = user.getChatGroupMemberships()
                .stream()
                .filter(chatGroupMembership ->
                        chatGroupMembership
                                .getChatGroup()
                                .getId().equals(message.getRoom().getChatGroup().getId())).findFirst().orElseThrow();

        return determinationOfAuthorityOfUser(membership, actionType);

    }

    @Override
    public boolean checkUserAuthorityToAccessRole(User user, Long roleId, String actionType) {
        ChatGroupRole chatGroupRole = this.groupRoleRepository.findById(roleId).orElseThrow();
        ChatGroupMembership membership = user.getChatGroupMemberships()
                .stream()
                .filter(chatGroupMembership ->
                        chatGroupMembership
                                .getChatGroup()
                                .getId()
                                .equals(chatGroupRole.getUserMembership().getChatGroup().getId())).findFirst().orElseThrow();
        return determinationOfAuthorityOfUser(membership, actionType);
    }
}
