package com.example.springchatserver.controller;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.RoomMessageDto;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.room.RoomMessageService;
import com.example.springchatserver.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/message")
public class ChatMessageController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMessageService messageService;


    @GetMapping("/byGroup/{groupId}/byRoom/{roomId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId,'READ_GROUP',#user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessRoom(#user, #roomId, 'READ_GROUP')")
    public ResponseEntity<List<RoomMessageDto>> getAllMessageOfRoom(@PathVariable Long groupId, @PathVariable Long roomId, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.roomService.getRoomById(roomId).getRoomMessages());
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @GetMapping("/byGroup/{groupId}/byRoom/{roomId}/byMessage/{messageId}")
    //@PreAuthorize("hasAuthorityToAccessGroup(#groupId,'READ_GROUP')")
    //@PostAuthorize("hasAuthorityToAccessMessage(#groupId,#roomId,#messageId,'READ_GROUP',#user)") // The method does not have the parameters what need for the authorization
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessMessage(#user, #messageId, 'READ_GROUP')")
    public ResponseEntity<RoomMessageDto> getMessageById(@PathVariable Long messageId, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.messageService.getRoomMessageById(messageId));
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @PostMapping
    //@PreAuthorize("hasAuthorityToAccessMessage(#roomMessageDto.groupId,#roomMessageDto.roomId,#roomMessageDto.messageId,'READ_GROUP',#user)")
    //@PreAuthorize("hasAuthorityToAccessGroup(#roomMessageDto.groupId,'READ_GROUP',#user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessGroup(#user, #roomMessageDto.groupId, 'READ_GROUP')")
    public ResponseEntity<RoomMessageDto> addNewMessage(@RequestBody RoomMessageDto roomMessageDto, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.messageService.saveNewMessage(user, roomMessageDto));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @PutMapping("/{messageId}")
    //@PreAuthorize("hasAuthorityToAccessMessage(#roomMessageDto.groupId,#roomMessageDto.roomId,#roomMessageDto.messageId,'READ_GROUP',#user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessMessage(#user, #messageId, 'UPDATE_MESSAGE')")
    public ResponseEntity<RoomMessageDto> updateMessage(@PathVariable Long messageId, @RequestBody RoomMessageDto roomMessageDto, @CurrentUser User user){
        try{
            return ResponseEntity.ok(this.messageService.updateMessage(messageId, roomMessageDto));
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
    @DeleteMapping("/byGroup/{groupId}/byRoom/{roomId}/byMessage/{messageId}")
    //@PreAuthorize("hasAuthorityToAccessMessage(#groupId,#roomId,#messageId,'READ_GROUP',#user)")
    @PreAuthorize("@groupSecurityService.checkUserAuthorityToAccessMessage(#user, #messageId, 'DELETE_MESSAGE')")
    public ResponseEntity<?> deleteMessage(@PathVariable Long groupId, @PathVariable Long roomId, @PathVariable Long messageId, @CurrentUser User user){
        // ez nem jó, küldök egy requestet egy olyan groupnak az id-jével ahol von authoritym és megadok bármilyen messageId-t és törli
        // megoldás: /byGroup/{groupId}/byRoom/{roomId}/byMessage/{messageId} és csinálni egy hasAuthorityToAccessMessage expression-t.
        try{
            this.messageService.deleteMessage(messageId);
            return ResponseEntity.ok().build();
        }catch (NoSuchElementException noSuchElementException){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
}
