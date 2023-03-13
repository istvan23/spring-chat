package com.example.springchatserver.repository;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.util.SampleGroupFactory;
import com.example.springchatserver.util.SampleUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomMessageRepository roomMessageRepository;
    @Autowired
    private GroupAnnouncementRepository groupAnnouncementRepository;

    @Test
    @Transactional
    void dbSaveGroupSuccessful(){
        User user = userRepository.save(SampleUserFactory.createFixRegularUser());
        String groupName = "Test chatgroup";

        ChatGroup group = SampleGroupFactory.createSimpleChatGroup(groupName, user);

        String announcementTitle = "Test title";
        String announcementText = "Test announcement";

        GroupAnnouncement groupAnnouncement = SampleGroupFactory.createSimpleAnnouncement(announcementTitle, announcementText, user, group);

        group.setGroupAnnouncements(List.of(groupAnnouncement));

        groupRepository.save(group);
        groupAnnouncementRepository.save(groupAnnouncement);

        List<ChatGroup> groups = StreamSupport.stream(groupRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertTrue(!groups.isEmpty());
        ChatGroup savedGroup = groups.stream().findFirst().get();
        assertNotNull(savedGroup.getId());
        assertTrue(savedGroup.getName().equals(groupName));
        assertTrue(savedGroup.getGroupAdministrator() == user);
        assertTrue(!savedGroup.getGroupAnnouncements().isEmpty());
        GroupAnnouncement savedAnnouncement = savedGroup.getGroupAnnouncements().stream().findFirst().get();
        assertNotNull(savedAnnouncement.getId());
        assertTrue(savedAnnouncement.getTitle().equals(announcementTitle) && savedAnnouncement.getText().equals(announcementText));
    }

    @Test
    @Transactional
    void dbSaveRoomSuccessful(){
        User user = userRepository.save(SampleUserFactory.createFixRegularUser());
        String groupName = "Test chatgroup";

        ChatGroup group = SampleGroupFactory.createSimpleChatGroup(groupName, user);

        String announcementTitle = "Test title";
        String announcementText = "Test announcement";

        GroupAnnouncement groupAnnouncement = SampleGroupFactory.createSimpleAnnouncement(announcementTitle, announcementText, user, group);

        group.setGroupAnnouncements(List.of(groupAnnouncement));

        groupRepository.save(group);
        groupAnnouncementRepository.save(groupAnnouncement);

        String roomName = "Test room";
        String testMessage = "Test message text";

        Room room = new Room();
        room.setChatGroup(group);
        room.setRoomName(roomName);
        room.setDateOfCreation(LocalDateTime.now());
        room.setRoomMessages(new ArrayList<>());
        group.setRooms(List.of(room));
        roomRepository.save(room);

        RoomMessage roomMessage = new RoomMessage();
        roomMessage.setRoom(room);
        LocalDateTime messageDate = LocalDateTime.now();
        roomMessage.setDateOfMessage(messageDate);
        roomMessage.setSender(user);
        roomMessage.setMessage(testMessage);
        room.setDateOfLastMessage(messageDate);
        room.setRoomMessages(List.of(roomMessage));
        roomMessageRepository.save(roomMessage);

        assertFalse(StreamSupport.stream(groupRepository.findAll().spliterator(), false).findFirst().get().getRooms().isEmpty());
        Room savedRoom = StreamSupport.stream(roomRepository.findAll().spliterator(), false).findFirst().get();
        assertNotNull(savedRoom.getId());
        assertTrue(savedRoom.getChatGroup() == group && savedRoom.getRoomName().equals(roomName));
        assertFalse(savedRoom.getRoomMessages().isEmpty());
        RoomMessage savedMessage = savedRoom.getRoomMessages().stream().findFirst().get();
        assertNotNull(savedMessage.getId());
        assertTrue(savedMessage.getRoom() == room);
        assertTrue(savedMessage.getMessage().equals(testMessage));
        assertTrue(savedMessage.getSender() == user);
    }

}