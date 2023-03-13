package com.example.springchatserver.controller;

import com.example.springchatserver.domain.Room;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.BasicChatGroupInformationDto;
import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.RoomDto;
import com.example.springchatserver.dto.RoomMessageDto;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.group.GroupMembershipService;
import com.example.springchatserver.service.group.GroupService;
import com.example.springchatserver.service.room.RoomMessageService;
import com.example.springchatserver.service.room.RoomService;
import com.example.springchatserver.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.ResponseEntity.status;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/group")
public class ChatGroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMessageService messageService;
    @Autowired
    private GroupMembershipService membershipService;
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")//("hasRole('ROLE_USER')")
    public ResponseEntity<List<ChatGroupDto>> getAllChatGroup(){
        return ResponseEntity.ok(this.groupService.getAllChatGroup());
    }
    @GetMapping({"/basicInformation","/basicInformation/byName/{chatGroupNameFilterText}"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<BasicChatGroupInformationDto>> getBasicInformationAboutFreeChatGroups(@PathVariable(required = false) String chatGroupNameFilterText)
    {
        return ResponseEntity.ok(this.groupService.getBasicInformationAboutChatGroups(chatGroupNameFilterText));
    }
    @GetMapping("/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId,'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'READ_GROUP')")
    public ResponseEntity<ChatGroupDto> getChatGroupById(@PathVariable Long groupId, @CurrentUser User user){
        try {
            return ResponseEntity.ok(this.groupService.getChatGroupById(groupId));
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewChatGroup(@RequestBody ChatGroupDto chatGroup, @CurrentUser User user){
        try{
            if (chatGroup.getGroupAdministratorId().equals(user.getId()) && chatGroup.getGroupAdministratorUsername().equals(user.getUsername())) {
                this.groupService.createNewChatGroup(chatGroup);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }


    }
    @PutMapping("/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId,'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'UPDATE_GROUP')")
    public ResponseEntity<ChatGroupDto> updateChatGroup(@PathVariable Long groupId, @RequestBody ChatGroupDto chatGroup, @CurrentUser User user){
        if (chatGroup.getId() != null && groupId.equals(chatGroup.getId())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        try {
            ChatGroupDto chatGroupDto = this.groupService.updateChatGroup(groupId, chatGroup);
            return ResponseEntity.ok(chatGroupDto);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @DeleteMapping("/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId,'DELETE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'DELETE_GROUP')")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId, @CurrentUser User user){
        try{
            groupService.deleteChatGroup(groupId);
            return ResponseEntity.ok(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
