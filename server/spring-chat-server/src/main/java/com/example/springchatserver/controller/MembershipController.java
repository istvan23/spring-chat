package com.example.springchatserver.controller;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.MembershipDto;
import com.example.springchatserver.dto.UserDto;
import com.example.springchatserver.dto.UserJoinGroupForm;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.group.GroupMembershipService;
import com.example.springchatserver.service.group.GroupRoleService;
import com.example.springchatserver.service.group.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/membership")
@Slf4j
public class MembershipController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupMembershipService membershipService;
    @Autowired
    private GroupRoleService groupRoleService;

    @GetMapping("/byGroup/{groupId}/byMembership/{membershipId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'READ_GROUP')")
    public ResponseEntity<MembershipDto> getMemberShipById(@PathVariable Long membershipId, @PathVariable Long groupId, @CurrentUser User user){
        MembershipDto membership;
        try{
            membership = this.membershipService.getChatGroupMembershipById(membershipId);
        }
        catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(membership);
    }
    @GetMapping("/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'READ_GROUP', #user) or hasRole('ROLE_USER')")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'READ_GROUP')")
    public ResponseEntity<List<MembershipDto>> getMembersOfGroup(@PathVariable Long groupId, @CurrentUser User user){
        List<MembershipDto> membershipsOfGroup;
        try {
            membershipsOfGroup = this.membershipService.getMembersOfChatGroup(groupId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(membershipsOfGroup);
    }
    @GetMapping("/byUser/{userId}")
    //@PreAuthorize("hasAuthorityToAccessUserById(#userId, #user) or hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MembershipDto>> getGroupMembershipsOfUser(@PathVariable Long userId, @CurrentUser User user){
        List<MembershipDto> membershipsOfUser;
        try {
            membershipsOfUser = this.membershipService.getGroupMembershipsOfUser(userId);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(membershipsOfUser);
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MembershipDto> addNewMemberToGroup(@RequestBody UserJoinGroupForm newMember, @CurrentUser User user){
        try {
            MembershipDto membership = this.membershipService.addNewMemberToGroup(newMember.getGroupId(), newMember.getUserId());
            return ResponseEntity.ok().body(membership);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{membershipId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#updatableMembership.chatGroup.groupId, 'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessMembership(#user, #membershipId, 'UPDATE_GROUP')")
    public ResponseEntity<MembershipDto> updateMembership(@PathVariable Long membershipId, @RequestBody MembershipDto updatableMembership, @CurrentUser User user){
        MembershipDto updated = null;
        try {
            updated = this.membershipService.updateMembership(membershipId ,updatableMembership);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/byUser/{userId}/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'UPDATE_GROUP', #user) or hasAuthorityToAccessUserById(#userId, #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'UPDATE_GROUP') or #user.id==#userId")
    public ResponseEntity<?> removeMemberFromGroup(@PathVariable Long userId, @PathVariable Long groupId, @CurrentUser User user){
        try {
            this.membershipService.removeMemberFromGroup(groupId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
