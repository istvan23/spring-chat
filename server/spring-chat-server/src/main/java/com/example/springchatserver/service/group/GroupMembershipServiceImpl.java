package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.MembershipDto;
import com.example.springchatserver.mapper.ChatGroupMapper;
import com.example.springchatserver.mapper.MembershipMapper;
import com.example.springchatserver.mapper.UserMapper;
import com.example.springchatserver.repository.GroupMembershipRepository;
import com.example.springchatserver.repository.GroupRepository;
import com.example.springchatserver.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GroupMembershipServiceImpl implements GroupMembershipService{

    private final GroupRepository groupRepository;
    private final ChatGroupMapper chatGroupMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final GroupMembershipRepository groupMembershipRepository;
    private final MembershipMapper membershipMapper;

    public GroupMembershipServiceImpl(GroupRepository groupRepository, ChatGroupMapper chatGroupMapper, UserRepository userRepository, UserMapper userMapper, GroupMembershipRepository groupMembershipRepository, MembershipMapper membershipMapper) {
        this.groupRepository = groupRepository;
        this.chatGroupMapper = chatGroupMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.groupMembershipRepository = groupMembershipRepository;
        this.membershipMapper = membershipMapper;
    }


    private boolean checkIfTheUserHasMembership(User user, ChatGroup chatGroup){
        return chatGroup.getMembers().stream().anyMatch(member -> member.getUser().equals(user));
    }

    @Override
    public MembershipDto getChatGroupMembershipById(Long membershipId) {
        ChatGroupMembership chatGroupMembership = this.groupMembershipRepository.findById(membershipId).orElseThrow(NoSuchElementException::new);
        MembershipDto membershipDto = this.membershipMapper.convertToDto(chatGroupMembership);
        return membershipDto;
    }

    @Override
    public List<MembershipDto> getMembersOfChatGroup(Long groupId) {
        List<MembershipDto> memberships = this.groupRepository.findById(groupId)
                .orElseThrow(NoSuchElementException::new)
                .getMembers()
                .stream()
                .map(chatGroupMembership -> this.membershipMapper.convertToDto(chatGroupMembership))
                .collect(Collectors.toList());
        return memberships;
    }

    @Override
    public MembershipDto addNewMemberToGroup(Long groupId, Long userId) throws Exception {
        ChatGroup chatGroup = this.groupRepository.findById(groupId).orElseThrow(NoSuchElementException::new);

        User user = this.userRepository.findById(userId).orElseThrow(NoSuchElementException::new);


        if (checkIfTheUserHasMembership(user, chatGroup)){
            throw new Exception("This user already has membership in the given group");
        }

        LocalDateTime dateTime = LocalDateTime.now();

        ChatGroupMembership newMembership = new ChatGroupMembership();
        newMembership.setUser(user);
        newMembership.setChatGroup(chatGroup);
        newMembership.setDateOfJoin(dateTime);


        chatGroup.getMembers().add(newMembership);
        user.getChatGroupMemberships().add(newMembership);

        newMembership = this.groupMembershipRepository.save(newMembership);

        this.userRepository.save(user);
        this.groupRepository.save(chatGroup);

        return this.membershipMapper.convertToDto(newMembership);
    }

    @Override
    public ChatGroupDto removeMemberFromGroup(Long groupId, Long userId) throws Exception {
        ChatGroup chatGroup = this.groupRepository.findById(groupId).orElseThrow(NoSuchElementException::new);

        User user = this.userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        if(!checkIfTheUserHasMembership(user, chatGroup)) {
            throw new Exception("This user does not have membership in the given group!");
        }

        chatGroup.getMembers().remove(user);
        user.getChatGroupMemberships().remove(chatGroup);

        userRepository.save(user);
        ChatGroup updated = groupRepository.save(chatGroup);

        return this.chatGroupMapper.convertToDto(updated);
    }

    @Override
    public MembershipDto updateMembership(Long id, MembershipDto updatableMembership) throws Exception {
        if (!updatableMembership.getId().equals(id)){
            throw new Exception("Incoherent id parameter");
        }

        ChatGroupMembership chatGroupMembership = this.groupMembershipRepository.findById(id).orElseThrow(NoSuchElementException::new);
        ChatGroupRole role = chatGroupMembership.getRole();
        if(role == null){
            throw new Exception("This member does not have a group role.");
        }
        role.setName(updatableMembership.getRole().getName());
        role.setDisplayName(updatableMembership.getRole().getDisplayName());
        role.setAuthorities(updatableMembership.getRole().getAuthorities().stream().map(privilegeDto -> {
            ChatGroupPrivilege chatGroupPrivilege = new ChatGroupPrivilege();
            if (privilegeDto.getId() != null){
                chatGroupPrivilege.setId(privilegeDto.getId());
                chatGroupPrivilege.setRole(role);
                chatGroupPrivilege.setName(privilegeDto.getName());
            }
            return chatGroupPrivilege;
        }).collect(Collectors.toList()));
        chatGroupMembership.setRole(role);
        chatGroupMembership = this.groupMembershipRepository.save(chatGroupMembership);
        return this.membershipMapper.convertToDto(chatGroupMembership);
    }

    @Override
    public List<MembershipDto> getGroupMembershipsOfUser(Long userId) {
        List<MembershipDto> memberships = this.userRepository.findById(userId)
                .get()
                .getChatGroupMemberships()
                .stream()
                .map(chatGroupMembership -> this.membershipMapper.convertToDto(chatGroupMembership))
                .collect(Collectors.toList());
        return memberships;
    }
}
