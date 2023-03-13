package com.example.springchatserver.mapper;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
// mockolt repositoryval

@SpringBootTest(classes = {UserMapperImpl.class, ChatGroupMapperImpl.class, RoomMapperImpl.class, RoomMessageMapperImpl.class, RoleMapperImpl.class, GroupAnnouncementMapperImpl.class})
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChatGroupMapper chatGroupMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private RoomMessageMapper roomMessageMapper;
    @Autowired
    private GroupAnnouncementMapper groupAnnouncementMapper;

    @Test
    void mapperTest(){
        LocalDateTime dateTime = LocalDateTime.now();

        User user = new User();
        user.setId(1l);
        user.setUsername("testuser");
        user.setPassword("p4ssw0rd");

        Role role = new Role();
        role.setId(1l);
        role.setName("ROLE_TEST");
        role.setDisplayName("Test role");
        AppPrivilege privilege = new AppPrivilege();
        privilege.setId(1l);
        privilege.setName("USER");
        privilege.setRole(role);
        role.setAuthorities(List.of(privilege));

        user.setRoles(List.of(role));
        user.setEmail("testmail@mail.com");

        ChatGroupMembership chatGroupMembership = new ChatGroupMembership();
        chatGroupMembership.setUser(user);
        chatGroupMembership.setDateOfJoin(dateTime);
        chatGroupMembership.setId(1l);
        ChatGroupRole chatGroupRole = new ChatGroupRole();
        chatGroupRole.setName("ROLE_FOUNDER");
        chatGroupRole.setDisplayName("Founder");
        chatGroupRole.setUserMembership(chatGroupMembership);
        chatGroupRole.setId(1l);
        chatGroupMembership.setRole(chatGroupRole);

        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setId(1l);
        chatGroup.setName("Testgroup");
        chatGroup.setGroupAdministrator(user);
        chatGroup.setMembers(List.of(chatGroupMembership));
        chatGroup.setDateOfCreation(dateTime);

        Room room = new Room();
        room.setId(1l);
        room.setRoomName("Test room");
        room.setDateOfCreation(dateTime);
        room.setDateOfLastMessage(dateTime);
        room.setChatGroup(chatGroup);

        RoomMessage roomMessage1 = new RoomMessage();
        roomMessage1.setId(1l);
        roomMessage1.setRoom(room);
        roomMessage1.setDateOfMessage(dateTime);
        roomMessage1.setSender(user);
        roomMessage1.setMessage("Test message 1");

        RoomMessage roomMessage2 = new RoomMessage();
        roomMessage2.setId(2l);
        roomMessage2.setRoom(room);
        roomMessage2.setDateOfMessage(dateTime);
        roomMessage2.setSender(user);
        roomMessage2.setMessage("Test message 1");

        GroupAnnouncement groupAnnouncement = new GroupAnnouncement();
        groupAnnouncement.setId(1l);
        groupAnnouncement.setTitle("Test announcement");
        groupAnnouncement.setText("Test text");
        groupAnnouncement.setAuthor(user);
        groupAnnouncement.setDateOfCreation(dateTime);
        groupAnnouncement.setDateOfLastModification(dateTime);
        groupAnnouncement.setChatGroup(chatGroup);



        room.setRoomMessages(List.of(roomMessage1, roomMessage2));

        chatGroup.setRooms(List.of(room));

        chatGroup.setGroupAnnouncements(List.of(groupAnnouncement));

        user.setChatGroupMemberships(List.of(chatGroupMembership));


        UserDto userDto = userMapper.convertToDto(user);
        ChatGroupDto chatGroupDto = chatGroupMapper.convertToDto(chatGroup);
        RoomDto roomDto = roomMapper.convertToDto(room);
        RoomMessageDto roomMessageDto1 = roomMessageMapper.convertToDto(roomMessage1);
        RoomMessageDto roomMessageDto2 = roomMessageMapper.convertToDto(roomMessage2);
        GroupAnnouncementDto groupAnnouncementDto = groupAnnouncementMapper.convertToDto(groupAnnouncement);
        System.out.println("test");
        assertTrue(userDto != null);

    }
}
