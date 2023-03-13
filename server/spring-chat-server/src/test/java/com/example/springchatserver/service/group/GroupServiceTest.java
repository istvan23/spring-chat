package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.mapper.ChatGroupMapper;
import com.example.springchatserver.repository.UserRepository;
import com.example.springchatserver.util.SampleUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.springchatserver.util.SampleGroupFactory.createSimpleChatGroupDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupServiceTest {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserRepository userRepository;

    private List<User> sampleUsers;

    @BeforeEach
    void setUp() {
        /*User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("p4ssw0rd1");
        user1.setEmail("user1@mail.com");
        user1.set*/
        User user1 = SampleUserFactory.createRegularUser("user1", "p4ssw0rd1", "user1@mail.com");
        User user2 = SampleUserFactory.createRegularUser("user2", "p4ssw0rd2", "user2@mail.com");

        sampleUsers = StreamSupport.stream(userRepository.saveAll(List.of(user1,user2)).spliterator(), false).collect(Collectors.toList());
    }

    @Test
    @Transactional
    void shouldCreateChatGroup(){
        ChatGroupDto chatGroupDto = createSimpleChatGroupDto("testChatGroup", sampleUsers.get(0));

        this.groupService.createNewChatGroup(chatGroupDto);

        ChatGroupDto saved = groupService.getChatGroupById(0l);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(saved.getName(), chatGroupDto.getName());
        assertEquals(saved.getGroupAdministratorId(), sampleUsers.get(0).getId());
        assertEquals(saved.getGroupAdministratorUsername(), sampleUsers.get(0).getUsername());
    }

    @Test
    @Transactional
    void shouldUpdateChatGroup(){
        ChatGroupDto chatGroupDto = groupService.createNewChatGroup(createSimpleChatGroupDto("testChatGroup", sampleUsers.get(0)));
        String newName = "updatedTestChatGroup";
        chatGroupDto.setName(newName);

        ChatGroupDto saved = this.groupService.updateChatGroup(chatGroupDto.getId(), chatGroupDto);
        assertEquals(saved.getName(), newName);
    }

    @Test
    @Transactional
    void shouldDeleteChatGroup(){
        ChatGroupDto chatGroupDto = groupService.createNewChatGroup(createSimpleChatGroupDto("testChatGroup", sampleUsers.get(0)));
        groupService.deleteChatGroup(chatGroupDto.getId());

        List<ChatGroupDto> list = groupService.getAllChatGroup();

        assertTrue(list.isEmpty());
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenTryToCreateGroupWithAlreadyExistingName(){
        ChatGroupDto chatGroupDto = groupService.createNewChatGroup(createSimpleChatGroupDto("testChatGroup", sampleUsers.get(0)));
        //ChatGroupDto chatGroupWithSameName = groupService.createNewChatGroup(createSimpleChatGroupDto("testChatGroup", sampleUsers.get(1)));


        //assertThrowsExactly(groupService.createNewChatGroup(DataIntegrityViolationException.class, createSimpleChatGroupDto("testChatGroup", sampleUsers.get(1))))
    }

    @Test
    @Transactional
    void shouldReturnNullIfTryToGetChatGroupWithNonExistentId(){

    }
}