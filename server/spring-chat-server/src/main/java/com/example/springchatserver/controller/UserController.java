package com.example.springchatserver.controller;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.UserDto;
import com.example.springchatserver.security.CurrentUser;
import com.example.springchatserver.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    //@PreAuthorize("hasAuthorityToAccessUserById(#id, #user) or hasRole('ROLE_ADMIN')")
    @PreAuthorize("#id==#user.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id, @CurrentUser User user){
        try{
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthorityToAccessUserById(#id) or hasRole('ROLE_ADMIN')")
    @PreAuthorize("(#user.id==id and id==modifiedUser.id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, UserDto modifiedUser, @CurrentUser User user){
        try{
            return ResponseEntity.ok(userService.updateUserCredentials(id, modifiedUser));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthorityToAccessUserById(#id) or hasRole('ROLE_ADMIN')")
    @PreAuthorize("#user.id==id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id, @CurrentUser User user){
        try{
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDto userDto){
        try{
            this.userService.registerNewUser(userDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }
}
