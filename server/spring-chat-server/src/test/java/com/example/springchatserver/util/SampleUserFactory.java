package com.example.springchatserver.util;

import com.example.springchatserver.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class SampleUserFactory {
    public static User createFixAdminUser(PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("pw01234"));
        user.setEmail("admin@springchat.com");
        Role role = new Role();
        role.setUser(user);
        role.setName("ROLE_ADMIN");
        role.setDisplayName("Admin");
        AppPrivilege adminPrivilege = new AppPrivilege();
        adminPrivilege.setRole(role);
        adminPrivilege.setName("ROLE_ADMIN");
        role.setAuthorities(List.of(adminPrivilege));
        user.setRoles(List.of(role));
        user.setEnabled(true);
        return user;
    }

    public static User createFixRegularUser(){
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("p4ssw0rd");
        user.setEmail("test@testmail.com");
        user.setEnabled(true);
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDisplayName("USER");
        AppPrivilege privilege = new AppPrivilege();
        privilege.setName("ROLE_USER");
        privilege.setRole(role);
        role.setAuthorities(List.of(privilege));

        user.setRoles(List.of(role));
        return user;
    }

    public static User createRegularUser(String username, String password, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setEnabled(true);
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDisplayName("USER");
        AppPrivilege privilege = new AppPrivilege();
        privilege.setName("ROLE_USER");
        privilege.setRole(role);
        role.setAuthorities(List.of(privilege));
        role.setUser(user);
        user.setRoles(List.of(role));
        return user;
    }
    public static User createRegularUser(String username, String password, String email, boolean enabled){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setEnabled(enabled);
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDisplayName("USER");
        AppPrivilege privilege = new AppPrivilege();
        privilege.setName("ROLE_USER");
        privilege.setRole(role);
        role.setAuthorities(List.of(privilege));
        user.setRoles(List.of(role));
        return user;
    }
    public static User createRegularUser(String username, String password, String email, boolean enabled, List<Role> additionalRoles){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setEnabled(enabled);
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDisplayName("USER");
        AppPrivilege privilege = new AppPrivilege();
        privilege.setName("ROLE_USER");
        privilege.setRole(role);
        role.setAuthorities(List.of(privilege));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        roles.addAll(additionalRoles);
        user.setRoles(roles);
        return user;
    }
}
