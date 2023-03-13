package com.example.springchatserver.service.user;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);
    List<UserDto> getAllUser();
    void registerNewUser(UserDto userDto);
    UserDto updateUserCredentials(Long id, UserDto userDto);
    void deleteUserById(Long id);
    User getCurrentUser();

}
