package com.example.springchatserver.service.group;

import com.example.springchatserver.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupMembershipServiceTest {
    @Autowired
    private GroupMembershipService groupMembershipService;

    @Test
    @Transactional
    void shouldAddNewMemberToGroup(){
    }
}