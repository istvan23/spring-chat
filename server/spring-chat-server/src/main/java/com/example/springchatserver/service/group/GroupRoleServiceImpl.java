package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.dto.RoleDto;
import com.example.springchatserver.mapper.ChatGroupMapper;
import com.example.springchatserver.mapper.PrivilegeMapper;
import com.example.springchatserver.mapper.RoleMapper;
import com.example.springchatserver.mapper.UserMapper;
import com.example.springchatserver.repository.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GroupRoleServiceImpl implements GroupRoleService{

    private final GroupRepository groupRepository;
    private final ChatGroupMapper chatGroupMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final AppPrivilegeRepository appPrivilegeRepository;
    private final GroupPrivilegeRepository groupPrivilegeRepository;
    private final PrivilegeMapper privilegeMapper;
    private final GroupRoleRepository groupRoleRepository;

    public GroupRoleServiceImpl(GroupRepository groupRepository,
                                ChatGroupMapper chatGroupMapper,
                                UserRepository userRepository,
                                UserMapper userMapper,
                                RoleRepository roleRepository,
                                RoleMapper roleMapper, AppPrivilegeRepository appPrivilegeRepository, GroupPrivilegeRepository groupPrivilegeRepository, PrivilegeMapper privilegeMapper, GroupRoleRepository groupRoleRepository) {
        this.groupRepository = groupRepository;
        this.chatGroupMapper = chatGroupMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.appPrivilegeRepository = appPrivilegeRepository;
        this.groupPrivilegeRepository = groupPrivilegeRepository;
        this.privilegeMapper = privilegeMapper;
        this.groupRoleRepository = groupRoleRepository;
    }

    @Override
    public RoleDto getRoleById(Long id) {
        RoleDto roleDto = null;

        try{
            roleDto = this.roleMapper.convertToDto(this.groupRoleRepository.findById(id).get());
        }catch (DataAccessException e){
            throw e;
        }


        return roleDto;
    }


    @Override
    @Transactional
    public RoleDto addRoleForUser(Long userId, RoleDto role, Long groupId) throws Exception {
        User user = this.userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        ChatGroupMembership chatGroupMembership = user.getChatGroupMemberships()
                .stream()
                .filter(membership -> membership.getChatGroup().getId() == groupId)
                .findFirst().orElseThrow(() -> new Exception("User does not have membership or has not applied for membership."));
        if (chatGroupMembership.getRole() != null){
            throw new Exception("This user already has a role in this group.");
        }
        ChatGroupRole newRole = new ChatGroupRole();
        newRole.setName(role.getName());
        newRole.setDisplayName(role.getDisplayName());

        newRole.setUserMembership(chatGroupMembership);

        List<ChatGroupPrivilege> privileges = role.getAuthorities()
                .stream()
                .map(privilegeDto -> {
                    ChatGroupPrivilege chatGroupPrivilege = new ChatGroupPrivilege();
                    chatGroupPrivilege.setName(privilegeDto.getName());
                    chatGroupPrivilege.setRole(newRole);
                    return chatGroupPrivilege;
                })
                .collect(Collectors.toList());

        newRole.setAuthorities(privileges);
        try{
            this.groupPrivilegeRepository.saveAll(privileges);
        }
        catch (Exception e){
            throw e;
        }


        ChatGroupRole saved = null;
        try{
            saved = this.groupRoleRepository.save(newRole);
        }catch (DataIntegrityViolationException e){
            throw e;
        }
        return roleMapper.convertToDto(saved);
    }

    @Override
    public RoleDto updateRole(Long roleId, RoleDto role) throws Exception {
        if(!role.getId().equals(roleId)){
            throw new Exception("Incoherent parameter values");
        }
        ChatGroupRole roleEntity = this.groupRoleRepository.findById(roleId).orElseThrow(NoSuchElementException::new);
        roleEntity.setName(role.getName());
        roleEntity.setDisplayName(role.getDisplayName());
        roleEntity.getAuthorities().forEach(authority -> {

        });
        List<ChatGroupPrivilege> privileges = role.getAuthorities().stream().map(dto -> {
            ChatGroupPrivilege privilege = new ChatGroupPrivilege();
            privilege.setName(dto.getName());
            if(dto.getId() != null) privilege.setId(dto.getId());
            privilege.setRole(roleEntity);
            return privilege;
        }).collect(Collectors.toList());
        roleEntity.setAuthorities(privileges);
        ChatGroupRole updated = null;
        try{
            updated = this.groupRoleRepository.findById(roleId).get();
        }
        catch (DataIntegrityViolationException e){
            throw e;
        }
        return roleMapper.convertToDto(updated);
    }

    @Override
    public void deleteRole(Long roleId) {

        try {
            this.groupRoleRepository.deleteById(roleId);
        }catch (NoSuchElementException noSuchElementException){
            throw noSuchElementException;
        }
    }


}
