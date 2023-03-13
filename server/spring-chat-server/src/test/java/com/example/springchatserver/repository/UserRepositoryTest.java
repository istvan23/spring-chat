package com.example.springchatserver.repository;

import com.example.springchatserver.domain.ChatAuthority;
import com.example.springchatserver.domain.ChatBasicAuthority;
import com.example.springchatserver.domain.Role;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.util.SampleUserFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;



    @Test
    void dbSavedRegularUserSuccessful(){
        System.out.println("TEST RUNNING");
        /*
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("p4ssw0rd");
        user.setEmail("test@testmail.com");
        user.setEnabled(true);
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setDisplayName("User");
        List<ChatAuthority> authorityList = new ArrayList<>();
        authorityList.add(ChatBasicAuthority.USER);
        role.setAuthorities(authorityList);
        user.setRoles(List.of(role));
        */
        User user = SampleUserFactory.createFixRegularUser();
        userRepository.save(user);
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        boolean condition = !users.isEmpty() && users.get(0).getId() != null;
        assertTrue(condition);
    }

    @Test
    void canSaveMultipleUserObject(){
        User user1 = SampleUserFactory.createRegularUser("tesztelek","jelszo", "tesztelek@mail.com");
        userRepository.save(user1);
        User user2 = SampleUserFactory.createRegularUser("tesztelek2","p4ssw0rd", "tesztelek2@mail.com");
        userRepository.save(user2);
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertTrue(users.size() == 2);
    }

    @Test
    void doNotSaveTwoUserObjectWithTheSameUserName(){
        User user1 = SampleUserFactory.createRegularUser("tesztelek","jelszo", "tesztelek@mail.com");
        userRepository.save(user1);
        User user2 = SampleUserFactory.createRegularUser("tesztelek","p4ssw0rd", "tesztelek2@mail.com");
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2));
    }

    @Test
    void ableToFindUserByUsername(){
        User user1 = SampleUserFactory.createRegularUser("tesztelek","jelszo", "tesztelek@mail.com");
        userRepository.save(user1);
        User user2 = SampleUserFactory.createRegularUser("tesztelek2","p4ssw0rd", "tesztelek2@mail.com");
        userRepository.save(user2);
        User user3 = SampleUserFactory.createRegularUser("tesztelek3","jelszo2", "tesztelek3@mail.com");
        userRepository.save(user3);
        User user4 = SampleUserFactory.createRegularUser("tesztelek4","p4ssw0rd2", "tesztelek4@mail.com");
        userRepository.save(user4);
        User user5 = SampleUserFactory.createRegularUser("tesztelek5","jelszo3", "tesztelek5@mail.com");
        userRepository.save(user5);

        User searchedUser = userRepository.findByUsername("tesztelek4").get();
        assertTrue(searchedUser != null && searchedUser.getUsername() == "tesztelek4");

    }

    @Test
    void ableToDeleteAnUserFromTheFiveUserItemLengthDb(){
        User user1 = SampleUserFactory.createRegularUser("tesztelek","jelszo", "tesztelek@mail.com");
        userRepository.save(user1);
        User user2 = SampleUserFactory.createRegularUser("tesztelek2","p4ssw0rd", "tesztelek2@mail.com");
        userRepository.save(user2);
        User user3 = SampleUserFactory.createRegularUser("tesztelek3","jelszo2", "tesztelek3@mail.com");
        userRepository.save(user3);
        User user4 = SampleUserFactory.createRegularUser("tesztelek4","p4ssw0rd2", "tesztelek4@mail.com");
        userRepository.save(user4);
        User user5 = SampleUserFactory.createRegularUser("tesztelek5","jelszo3", "tesztelek5@mail.com");
        userRepository.save(user5);

        User searchedUser = userRepository.findByUsername("tesztelek4").get();
        userRepository.deleteById(searchedUser.getId());

        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertTrue(users.size() == 4);
        assertTrue(userRepository.findByUsername("tesztelek4").isEmpty());
    }

    @Test
    void ableToChangePasswordField(){
        User user = SampleUserFactory.createRegularUser("tesztelek","jelszo", "tesztelek@mail.com");
        user = userRepository.save(user);
        user.setPassword("j3lsz0");
        //userRepository.save(user); // ezen elhasal
        User userObjectFromDb = userRepository.findByUsername("tesztelek").get(); // azt tapasztaltam hogy már save nélkül is megváltozott a jelszó, wtf.
        assertTrue(userObjectFromDb.getPassword().equals("j3lsz0"));
    }
}