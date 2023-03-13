package com.example.springchatserver.controller;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.RoomDto;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.group.GroupService;
import com.example.springchatserver.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private RoomService roomService;

    @GetMapping("/{roomId}")
    //@PreAuthorize("hasAuthorityToAccessRoom(#roomId,'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRoom(#user, #roomId, 'READ_GROUP')")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId, @CurrentUser User user){
        try{
            RoomDto room = this.roomService.getRoomById(roomId);
            return ResponseEntity.ok(room);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @GetMapping("/byGroup/{groupId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId,'READ_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #groupId, 'READ_GROUP')")
    public ResponseEntity<List<RoomDto>> getAllRoomOfGroup(@PathVariable Long groupId, @CurrentUser User user){
        try{
            List<RoomDto> allRoomOfGroup = this.roomService.getAlRoomByGroupId(groupId);
            return ResponseEntity.ok(allRoomOfGroup);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @PostMapping
    //@PreAuthorize("hasAuthorityToAccessGroup(#newRoom.chatGroupId,'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #newRoom.chatGroupId, 'UPDATE_GROUP')")
    public ResponseEntity<RoomDto> createNewRoomWithinGroup(@RequestBody RoomDto newRoom, @CurrentUser User user){
        try{
            RoomDto room = this.roomService.createRoom(newRoom);
            return ResponseEntity.ok(room);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @PutMapping("/{roomId}")
    //@PreAuthorize("hasAuthorityToAccessRoom(#roomId,'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRoom(#user, #roomId, 'UPDATE_GROUP')")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long roomId, @RequestBody RoomDto updatableRoom, @CurrentUser User user){
        try{
            RoomDto room = this.roomService.updateRoom(roomId, updatableRoom);
            return ResponseEntity.ok(room);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @DeleteMapping("/{roomId}")
    //@PreAuthorize("hasAuthorityToAccessRoom(#roomId,'UPDATE_GROUP', #user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRoom(#user, #roomId, 'UPDATE_GROUP')")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId, @CurrentUser User user){
        try{
            this.roomService.deleteRoom(roomId);
            return ResponseEntity.ok(HttpStatus.OK);
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
}
