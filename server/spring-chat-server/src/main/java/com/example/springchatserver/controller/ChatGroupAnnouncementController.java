package com.example.springchatserver.controller;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.GroupAnnouncementDto;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.group.GroupAnnouncementService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/announcement")
public class ChatGroupAnnouncementController {
    @Autowired
    private GroupAnnouncementService groupAnnouncementService;

    @GetMapping("/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'READ_GROUP')")
    public ResponseEntity<List<GroupAnnouncementDto>> getAllAnnouncementOfGroup(@PathVariable Long groupId, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.groupAnnouncementService.getAllAnnouncementsOfGroupById(groupId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{announcementId}/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroupAnnouncement(#user, #announcementId, 'READ_GROUP')")
    public ResponseEntity<GroupAnnouncementDto> getAnnouncement(@PathVariable Long announcementId, @PathVariable Long groupId, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.groupAnnouncementService.getAnnouncementById(announcementId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    //@PreAuthorize("hasAuthorityToAccessGroup(#announcement.chatGroupId, 'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #announcement.chatGroupId, 'UPDATE_GROUP')")
    public ResponseEntity<GroupAnnouncementDto> addNewAnnouncement(@RequestBody GroupAnnouncementDto announcement, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.groupAnnouncementService.addNewAnnouncement(announcement));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping
    //@PreAuthorize("hasAuthorityToAccessGroupAnnouncement(#announcement.chatGroupId, #announcement.id, 'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroupAnnouncement(#user, #announcement.id, 'UPDATE_GROUP')")
    public ResponseEntity<GroupAnnouncementDto> updateAnnouncement(@RequestBody GroupAnnouncementDto announcement, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.groupAnnouncementService.updateAnnouncement(announcement));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{announcementId}/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId, 'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroupAnnouncement(#user, #announcementId, 'UPDATE_GROUP')")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long groupId, @PathVariable Long announcementId, @CurrentUser User user){
        try{
            this.groupAnnouncementService.deleteAnnouncement(announcementId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
