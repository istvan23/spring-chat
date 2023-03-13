package com.example.springchatserver.controller;

import com.example.springchatserver.dto.LoginRequest;
import com.example.springchatserver.dto.MembershipDto;
import com.example.springchatserver.dto.UserJoinGroupForm;
import com.example.springchatserver.util.TestDbInitializer;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MembershipControllerTest {
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
    void groupMemberCanGetMembersOfGroupWhereHasMembership() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byGroup/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        List<MembershipDto> members = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), new TypeReference<List<MembershipDto>>(){});
        assertTrue(members != null && !members.isEmpty());
        MembershipDto testUserOwnMembership = members.stream().filter(membershipDto -> membershipDto.getUsername().equals(loginRequest.getUsername())).findFirst().get();
        assertTrue(testUserOwnMembership != null);

    }
    @Test
    void regularUserTryGetMembersOfAGroupWhereDoNotHasMembership() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byGroup/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void regularUserTryGetMembersOfAGroupThatNotExistAndTheServerMustReturnForbidden() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byGroup/5").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    //átrakni membershipcontroller testbe
    @Test
    void adminTryGetMembersOfAGroupThatNotExistAndTheServerMustReturnNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "pw01234");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byGroup/5").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void userGetHisOwnMembershipsList() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byUser/3").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();

        List<MembershipDto> membershipList = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), new TypeReference<List<MembershipDto>>() {
        });
        assertNotNull(membershipList);
        assertTrue(!membershipList.isEmpty());
        assertTrue(membershipList.stream().findFirst().get().getUsername().equals(loginRequest.getUsername()));
    }

    @Test
    void userTryToGetAnotherUserMembershipListAndServerShouldForbidden() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/membership/byUser/4").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void userJoinGroupAndServerShouldReturnHisMembershipDto() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        UserJoinGroupForm userJoinGroupForm = new UserJoinGroupForm(2l,1l);

        MvcResult joinResult = this.mockMvc.perform(post("/api/membership/").header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(userJoinGroupForm)))
                .andExpect(status().isOk())
                .andReturn();
        MembershipDto membership = this.objectMapper.readValue(joinResult.getResponse().getContentAsString(), MembershipDto.class);
        assertNotNull(membership);
        assertNotNull(membership.getId());
        assertTrue(membership.getUsername().equals(loginRequest.getUsername()));
        assertNotNull(membership.getUserId());
        assertNotNull(membership.getChatGroup());
    }

    @Test
    void userCanDeleteHisMembership() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult deleteRequestResult = this.mockMvc.perform(delete("/api/membership/byUser/3/byGroup/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void userTryDeleteOtherUserMembershipWithinTheSameGroupAndServerShouldForbidden() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult deleteRequestResult = this.mockMvc.perform(delete("/api/membership/byUser/4/byGroup/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void userTryDeleteOtherUserMembershipInAnotherGroupAndServerShouldForbidden() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult deleteRequestResult = this.mockMvc.perform(delete("/api/membership/byUser/1/byGroup/1").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}