package com.example.springchatserver.service.user;

import com.example.springchatserver.domain.AppPrivilege;
import com.example.springchatserver.domain.Role;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.UserDto;
import com.example.springchatserver.mapper.UserMapper;
import com.example.springchatserver.repository.AppPrivilegeRepository;
import com.example.springchatserver.repository.RoleRepository;
import com.example.springchatserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AppPrivilegeRepository appPrivilegeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDto getUserById(Long id) {
        User user = this.userRepository.findById(id).get();
        UserDto userDto = this.userMapper.convertToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUser() {
        return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .map(user -> this.userMapper.convertToDto(user)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void registerNewUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        Role role = new Role();
        role.setDisplayName("User");
        role.setName("ROLE_USER");
        AppPrivilege appPrivilege = new AppPrivilege();
        appPrivilege.setName("ROLE_USER");
        appPrivilege.setRole(role);
        role.setAuthorities(List.of(appPrivilege));
        role.setUser(user);
        user.setRoles(List.of(role));
        user.setEnabled(true);


        this.roleRepository.save(role);
        this.appPrivilegeRepository.save(appPrivilege);

        this.userRepository.save(user);
    }

    @Override
    public UserDto updateUserCredentials(Long id, UserDto userDto) {
        User user = this.userRepository.findById(id).get();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user = this.userRepository.save(user);
        return this.userMapper.convertToDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = this.userRepository.findByUsername(authentication.getName()).get();
        return currentUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = this.userRepository.findByUsername(username).get();
        if (userDetails == null){
            throw new UsernameNotFoundException("User does not exist");
        }
        return userDetails;
    }
}
