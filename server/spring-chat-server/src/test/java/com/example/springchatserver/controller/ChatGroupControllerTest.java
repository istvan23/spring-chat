package com.example.springchatserver.controller;

import com.example.springchatserver.dto.ChatGroupDto;
import com.example.springchatserver.dto.LoginRequest;
import com.example.springchatserver.dto.MembershipDto;
import com.example.springchatserver.util.TestDbInitializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatGroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDbInitializer testDbInitializer;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @BeforeAll
    void setup(){
        testDbInitializer.initDbWithPreConstructedGroupSamples();
        testDbInitializer.initDbWithPreConstructedUserSamples();
    }
    @Test
    void shouldDenyAccessToGetChatGroupWithoutAuthorization() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/group/0")).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void shouldDenyAccessToGroupIfTheRequesterDoesNotHaveMembership() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        MvcResult result = this.mockMvc.perform(get("/api/group/1").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isUnauthorized()).andReturn();
    }
    @Test
    void groupFounderShouldGetSuccessfullyTheChatGroup() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "p4ssw0rd");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        MvcResult result = this.mockMvc.perform(get("/api/group/1").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        ChatGroupDto chatGroupDto = this.objectMapper.readValue(result.getResponse().getContentAsString(), ChatGroupDto.class);
        assertTrue(chatGroupDto != null);
        assertTrue(chatGroupDto.getName().equals("Testgroup"));
    }

    @Test
    void groupFounderCanDeleteItsGroup() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "p4ssw0rd");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult result = this.mockMvc.perform(delete("/api/group/1").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void groupFounderCanModifyItsGroupMainParameters() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "p4ssw0rd");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        ChatGroupDto updatable = new ChatGroupDto();
        updatable.setName("Updated name");

        MvcResult result = this.mockMvc.perform(put("/api/group/1").header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatable)))
                .andExpect(status().isOk())
                .andReturn();
        ChatGroupDto updatedChatGroup = this.objectMapper.readValue(result.getResponse().getContentAsString(), ChatGroupDto.class);
        assertTrue(updatedChatGroup.getId().equals(1l));
        assertTrue(updatedChatGroup.getName().equals(updatable.getName()));
    }

    @Test
    void groupFounderCanNotChangeNameOfGroupIfAlreadyAGroupExistWithThatName() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "p4ssw0rd");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        ChatGroupDto updatable = new ChatGroupDto();
        updatable.setName("tesztelek3group");

        MvcResult result = this.mockMvc.perform(put("/api/group/1").header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatable)))
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }
    @Test
    void groupFounderCanNotChangeAnotherGroupWhatNotItsOwn() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "p4ssw0rd");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        ChatGroupDto updatable = new ChatGroupDto();
        updatable.setName("not tesztelek3's group");

        MvcResult result = this.mockMvc.perform(put("/api/group/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatable)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void groupFounderCanNotDeleteAnotherGroupWhatNotItsOwn() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "p4ssw0rd");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult result = this.mockMvc.perform(delete("/api/group/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void regularUserCanCreateGroupSuccessfully() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        ChatGroupDto chatGroupDto = new ChatGroupDto();
        chatGroupDto.setName("Newchatgroup");
        chatGroupDto.setGroupAdministratorUsername(loginRequest.getUsername());
        MvcResult result = this.mockMvc.perform(post("/api/group/").header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chatGroupDto))
                )
                    .andExpect(status().isCreated())
                    .andReturn();

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byUser/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        List<MembershipDto> membershipDtos = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), new TypeReference<List<MembershipDto>>() {
        });
        MembershipDto savedGroupMembership = membershipDtos.stream().findFirst().get();
        assertTrue(savedGroupMembership.getId() != null);
        assertTrue(savedGroupMembership.getChatGroup().getId() != null);
        assertTrue(savedGroupMembership.getDateOfJoin() != null);
        assertTrue(savedGroupMembership.getRole() != null);
        assertTrue(savedGroupMembership.getRole().getAuthorities() != null && !savedGroupMembership.getRole().getAuthorities().isEmpty());
        assertTrue(savedGroupMembership.getChatGroup().getName().equals(chatGroupDto.getName()));
        assertTrue(savedGroupMembership.getChatGroup().getGroupAdministratorUsername().equals(loginRequest.getUsername()));
        assertTrue(savedGroupMembership.getChatGroup().getGroupAdministratorId() != null);
        assertTrue(savedGroupMembership.getChatGroup().getDateOfCreation() != null);
    }


}