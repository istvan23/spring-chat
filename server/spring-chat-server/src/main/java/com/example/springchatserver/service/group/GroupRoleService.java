package com.example.springchatserver.service.group;

import com.example.springchatserver.dto.RoleDto;

public interface GroupRoleService {
    RoleDto getRoleById(Long id);
    //List<RoleDto> getAllGroupRoleByGroupId(Long groupId);
    //RoleDto createNewGroupRole(Long groupId, RoleDto roleDto);
    //void deleteGroupRole(Long groupId, Long roleId);
    RoleDto addRoleForUser(Long userId, RoleDto role, Long groupId) throws Exception;
    RoleDto updateRole(Long roleId, RoleDto role) throws Exception;
    //void deleteRole(Long roleId, Long userId);
    void deleteRole(Long roleId);
}
