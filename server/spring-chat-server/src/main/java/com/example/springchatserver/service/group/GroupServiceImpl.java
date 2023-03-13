package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.dto.BasicChatGroupInformationDto;
import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.GroupAnnouncementDto;
import com.example.springchatserver.mapper.ChatGroupMapper;
import com.example.springchatserver.mapper.GroupAnnouncementMapper;
import com.example.springchatserver.repository.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GroupServiceImpl implements GroupService{

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupPrivilegeRepository groupPrivilegeRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupAnnouncementRepository groupAnnouncementRepository;
    private final GroupAnnouncementMapper groupAnnouncementMapper;
    private final ChatGroupMapper chatGroupMapper;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, GroupPrivilegeRepository groupPrivilegeRepository, GroupRoleRepository groupRoleRepository, GroupMembershipRepository groupMembershipRepository, GroupAnnouncementRepository groupAnnouncementRepository, GroupAnnouncementMapper groupAnnouncementMapper, ChatGroupMapper chatGroupMapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupPrivilegeRepository = groupPrivilegeRepository;
        this.groupRoleRepository = groupRoleRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.groupAnnouncementRepository = groupAnnouncementRepository;
        this.groupAnnouncementMapper = groupAnnouncementMapper;
        this.chatGroupMapper = chatGroupMapper;
    }

    @Transactional
    @Override
    public ChatGroupDto createNewChatGroup(ChatGroupDto newChatGroup) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ChatGroup chatGroup = new ChatGroup();
        User user = this.userRepository.findByUsername(newChatGroup.getGroupAdministratorUsername())
                .orElseThrow(NoSuchElementException::new);
        chatGroup.setName(newChatGroup.getName());
        chatGroup.setGroupAdministrator(user);

        ChatGroupMembership chatGroupMembership = new ChatGroupMembership();
        chatGroupMembership.setChatGroup(chatGroup);
        chatGroupMembership.setDateOfJoin(localDateTime);
        chatGroupMembership.setUser(user);

        ChatGroupRole role = new ChatGroupRole();
        role.setName("ROLE_FOUNDER");
        role.setDisplayName("Founder");
        role.setUserMembership(chatGroupMembership);
        ChatGroupPrivilege chatGroupPrivilege = new ChatGroupPrivilege();
        chatGroupPrivilege.setName("GROUP_ADMINISTRATOR");
        chatGroupPrivilege.setRole(role);
        role.setAuthorities(List.of(chatGroupPrivilege));

        chatGroup.setMembers(List.of(chatGroupMembership));
        chatGroup.setRooms(new ArrayList<>());
        chatGroup.setDateOfCreation(localDateTime);
        chatGroup.setGroupAnnouncements(new ArrayList<>());

        user.getChatGroupMemberships().add(chatGroupMembership);

        ChatGroupDto saved = null;
        try {
            chatGroup = this.groupRepository.save(chatGroup);
            this.groupMembershipRepository.save(chatGroupMembership);
            this.groupPrivilegeRepository.save(chatGroupPrivilege);
            this.groupRoleRepository.save(role);
            this.userRepository.save(user);
            saved = this.chatGroupMapper.convertToDto(chatGroup);

        }catch (DataIntegrityViolationException e){
            throw e;
        }
        return saved;
    }

    @Override
    public ChatGroupDto updateChatGroup(Long id, ChatGroupDto chatGroupDto) {
        ChatGroup chatGroup = this.groupRepository.findById(id).orElseThrow(NoSuchElementException::new);
        chatGroup.setName(chatGroupDto.getName());
        ChatGroupDto updated = null;
        try{
            updated = this.chatGroupMapper.convertToDto(this.groupRepository.save(chatGroup));
        }catch (DataIntegrityViolationException e){
            throw e;
        }
        return updated;
    }

    @Override
    public void deleteChatGroup(Long id) {
        try{
            this.groupRepository.deleteById(id);;
        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public ChatGroupDto getChatGroupById(Long id) {
        ChatGroupDto chatGroupDto = null;
        ChatGroup chatGroup = this.groupRepository.findById(id).get();
        if(chatGroup != null){
            chatGroupDto = this.chatGroupMapper.convertToDto(chatGroup);
        }
        return chatGroupDto;
    }

    @Override
    public List<ChatGroupDto> getAllChatGroup() {
        return StreamSupport
                .stream(this.groupRepository.findAll().spliterator(), false)
                .map(group -> chatGroupMapper.convertToDto(group))
                .collect(Collectors.toList());
    }

    @Override
    public List<BasicChatGroupInformationDto> getBasicInformationAboutChatGroups(String filterText) {
        List<BasicChatGroupInformationDto> chatGroups = null;
        if (filterText == null || filterText == ""){
            chatGroups = StreamSupport
                    .stream(this.groupRepository.findAll().spliterator(), false)
                    .map(group ->this.chatGroupMapper.convertToShallowDto(group))
                    .collect(Collectors.toList());
        }
        else {
            chatGroups = StreamSupport
                    .stream(this.groupRepository.findByNameContains(filterText).spliterator(), false)
                    .map(group ->this.chatGroupMapper.convertToShallowDto(group))
                    .collect(Collectors.toList());
        }
        return chatGroups;
    }


}
