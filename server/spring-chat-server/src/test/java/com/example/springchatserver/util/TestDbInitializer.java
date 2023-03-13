package com.example.springchatserver.util;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.dto.UserDto;
import com.example.springchatserver.repository.*;
import com.example.springchatserver.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class TestDbInitializer {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupMembershipRepository groupMembershipRepository;
    private RoleRepository roleRepository;
    private GroupRoleRepository groupRoleRepository;
    private AppPrivilegeRepository appPrivilegeRepository;
    private GroupPrivilegeRepository groupPrivilegeRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void initDbWithPreConstructedUserSamples(){
        List<User> users = List.of(
                SampleUserFactory.createRegularUser("tesztelek1", passwordEncoder.encode( "jelszo1"), "tesztelek1@mail.com"),
                SampleUserFactory.createRegularUser("tesztelek2", passwordEncoder.encode( "jelszo2"), "tesztelek2@mail.com"),
                SampleUserFactory.createRegularUser("tesztelek3", passwordEncoder.encode( "jelszo3"), "tesztelek3@mail.com"),
                SampleUserFactory.createFixAdminUser(this.passwordEncoder)
                );

        users.forEach( user -> {
            this.userRepository.save(user);
            user.getRoles().forEach(role -> {
                this.roleRepository.save(role);
                this.appPrivilegeRepository.saveAll(role.getAuthorities());
            });
        });
        this.addGroupTestSample(users.get(2), "tesztelek3group", List.of(users.get(1)));
        //userRepository.saveAll(users);
        System.out.println("test");
    }
    @Transactional
    public void initDbWithPreConstructedGroupSamples(){
        User user = SampleUserFactory.createFixRegularUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user = this.userRepository.save(user);
        ChatGroup chatGroup = SampleGroupFactory.createSimpleChatGroup("Testgroup", user);
        /*
        ChatGroupMembership chatGroupMembership = new ChatGroupMembership();
        chatGroupMembership.setChatGroup(chatGroup);
        chatGroupMembership.setUser(user);
        chatGroupMembership.setDateOfJoin(LocalDateTime.now());
        ChatGroupRole chatGroupRole = new ChatGroupRole();
        chatGroupRole.setName("ROLE_FOUNDER");
        chatGroupRole.setDisplayName("Founder");
        chatGroupRole.setUser(chatGroupMembership);

        ChatGroupPrivilege readPrivilege = new ChatGroupPrivilege();
        readPrivilege.setName("READ_GROUP");
        readPrivilege.setRole(chatGroupRole);

        ChatGroupPrivilege updatePrivilege = new ChatGroupPrivilege();
        updatePrivilege.setRole(chatGroupRole);
        updatePrivilege.setName("UPDATE_GROUP");

        ChatGroupPrivilege deletePrivilege = new ChatGroupPrivilege();
        deletePrivilege.setName("DELETE_GROUP");
        deletePrivilege.setRole(chatGroupRole);

        chatGroupRole.setAuthorities(List.of(readPrivilege, updatePrivilege, deletePrivilege));
        chatGroupMembership.setRole(chatGroupRole);
        */

        user = this.userRepository.save(user);
        user.getRoles().forEach(role -> {
            this.roleRepository.save(role);
            this.appPrivilegeRepository.saveAll(role.getAuthorities());
        });

        ChatGroupMembership chatGroupMembership = user.getChatGroupMemberships().stream().findFirst().get();
        chatGroupMembership.setChatGroup(chatGroup);
        chatGroup = this.groupRepository.save(chatGroup);

        //this.groupMembershipRepository.saveAll(user.getChatGroupMemberships());
        //ChatGroupMembership chatGroupMembership = user.getChatGroupMemberships().stream().findFirst().get();
        //chatGroupMembership.setChatGroup(chatGroup);
        this.groupMembershipRepository.save(chatGroupMembership);
        this.groupRoleRepository.save(chatGroupMembership.getRole());
        this.groupPrivilegeRepository.saveAll(chatGroupMembership.getRole().getAuthorities());




        //this.userRepository.save(user);

    }
    @Transactional
    private void addGroupTestSample(User user, String groupName, List<User> otherMembers){
        ChatGroup chatGroup = SampleGroupFactory.createSimpleChatGroup(groupName, user);
        ChatGroupMembership founderChatGroupMembership = user.getChatGroupMemberships().stream().findFirst().get();
        founderChatGroupMembership.setChatGroup(chatGroup);



        //this.userRepository.saveAll(otherMembers);

        ChatGroup ch = this.groupRepository.save(chatGroup);
        this.groupMembershipRepository.save(founderChatGroupMembership);
        this.groupRoleRepository.save(founderChatGroupMembership.getRole());
        this.groupPrivilegeRepository.saveAll(founderChatGroupMembership.getRole().getAuthorities());

        if (otherMembers != null && !otherMembers.isEmpty())
        {
            LocalDateTime dateTime = LocalDateTime.now();
            otherMembers.forEach(member -> {
                ChatGroupMembership chatGroupMembership = new ChatGroupMembership();
                chatGroupMembership.setChatGroup(ch);
                chatGroupMembership.setUser(member);
                chatGroupMembership.setDateOfJoin(dateTime);
                ChatGroupRole chatGroupRole = new ChatGroupRole();
                chatGroupRole.setName("GROUP_ROLE_MEMBER");
                chatGroupRole.setDisplayName("Member");
                chatGroupRole.setUserMembership(chatGroupMembership);
                ChatGroupPrivilege chatGroupPrivilege = new ChatGroupPrivilege();
                chatGroupPrivilege.setRole(chatGroupRole);
                chatGroupPrivilege.setName("READ_GROUP");
                chatGroupRole.setAuthorities(List.of(chatGroupPrivilege));
                chatGroupMembership.setRole(chatGroupRole);

                if (member.getChatGroupMemberships() == null) {
                    member.setChatGroupMemberships(List.of(chatGroupMembership));
                } else {
                    member.getChatGroupMemberships().add(chatGroupMembership);
                }



                this.groupMembershipRepository.save(chatGroupMembership);
                this.groupRoleRepository.save(chatGroupRole);
                this.groupPrivilegeRepository.save(chatGroupPrivilege);

                List<ChatGroupMembership> newMembershipList = new ArrayList<>();
                ch.getMembers().forEach(element -> newMembershipList.add(element));
                newMembershipList.add(chatGroupMembership);
                ch.setMembers(newMembershipList);

                //this.userRepository.save(member);
            });
            //List<User> forSave = new ArrayList(Arrays.asList(otherMembers));
            //this.userRepository.saveAll(forSave);
        }

    }
}
