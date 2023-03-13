package com.example.springchatserver.controller;

import com.example.springchatserver.domain.ChatGroupRole;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.RoleDto;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.group.GroupRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/groupRole")
@Slf4j
public class GroupRoleController {
    @Autowired
    private GroupRoleService groupRoleService;

    @GetMapping("/{roleId}/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroupRole(#roleId, #groupId, 'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRole(#user, #roleId, 'READ_GROUP')")
    public ResponseEntity<RoleDto> getGroupRoleById(@PathVariable Long roleId, @PathVariable Long groupId, @CurrentUser User user){
        try {
            return ResponseEntity.ok().body(this.groupRoleService.getRoleById(roleId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/byGroup/{groupId}/byUser/{userId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'UPDATE_GROUP', #requesterUser)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#requesterUser, #groupId, 'UPDATE_GROUP')")
    public ResponseEntity<RoleDto> addGroupRoleToUser(@PathVariable Long groupId, @PathVariable Long userId, @RequestBody RoleDto role, @CurrentUser User requesterUser){
        try {
            return ResponseEntity.ok().body(this.groupRoleService.addRoleForUser(userId, role, groupId));
        }catch (Exception e){
            log.error("!!!addGroupRoleToUser method error!!!", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{roleId}/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroupRole(#roleId, #groupId, 'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRole(#user, #roleId, 'UPDATE_GROUP')")
    public ResponseEntity<RoleDto> updateGroupRole(@PathVariable Long roleId, @PathVariable Long groupId, @RequestBody RoleDto role, @CurrentUser User user){
        try {
            return ResponseEntity.ok().body(this.groupRoleService.updateRole(roleId, role));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{roleId}/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroupRole(#roleId, #groupId, 'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRole(#user, #roleId, 'UPDATE_GROUP')")
    public ResponseEntity<?> deleteGroupRole(@PathVariable Long roleId, @PathVariable Long groupId, @CurrentUser User user){
        try {
            this.groupRoleService.deleteRole(roleId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
