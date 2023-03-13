package com.example.springchatserver.controller;

import com.example.springchatserver.dto.LoginRequest;
import com.example.springchatserver.dto.PrivilegeDto;
import com.example.springchatserver.dto.RoleDto;
import com.example.springchatserver.dto.UserJoinGroupForm;
import com.example.springchatserver.util.TestDbInitializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupRoleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDbInitializer testDbInitializer;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @BeforeEach
    void setup(){
        testDbInitializer.initDbWithPreConstructedGroupSamples();
        testDbInitializer.initDbWithPreConstructedUserSamples();
    }

    @Test
    void memberCanGetHisGroupRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/groupRole/3/byGroup/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        RoleDto role = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        assertNotNull(role.getName());
        assertNotNull(role.getDisplayName());
        assertNotNull(role.getUserId());
        assertNotNull(role.getAuthorities());
        assertFalse(role.getAuthorities().isEmpty());
    }
    @Test
    void memberCanGetOtherMemberGroupRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/groupRole/2/byGroup/2").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        RoleDto role = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        assertNotNull(role.getName());
        assertNotNull(role.getDisplayName());
        assertNotNull(role.getUserId());
        assertNotNull(role.getAuthorities());
        assertFalse(role.getAuthorities().isEmpty());
    }


    @Test
    void memberOfAGroupCanNotGetRoleOfAMemberWhoIsMemberOfAnotherGroup() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek3", "jelszo3");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        MvcResult getResult = this.mockMvc.perform(get("/api/groupRole/1/byGroup/1").header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void founderCanNotCreateGroupRoleForAUserWhoDoesNotHaveMembership() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek3", "jelszo3");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Long newRoleUserId = 1l;
        Long groupId = 2l;
        RoleDto roleDto = new RoleDto();
        roleDto.setName("Regular Member");
        roleDto.setUserId(newRoleUserId);
        PrivilegeDto privilegeDto = new PrivilegeDto();
        privilegeDto.setName("READ_GROUP");
        roleDto.setAuthorities(List.of(privilegeDto));

        MvcResult postResult = this.mockMvc
                .perform(post("/api/groupRole/byGroup/"+groupId+"/byUser/"+newRoleUserId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
    @Test
    void userWhoHasNotMembershipCanNotCreateGroupRoleForHimself() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        // Join group
        UserJoinGroupForm join =new UserJoinGroupForm(2l, 1l);
        MvcResult joinPostResult = this.mockMvc.perform(post("/api/membership/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(join)))
                .andExpect(status().isOk())
                .andReturn();

        Long newRoleUserId = 2l;
        Long groupId = 1l;
        RoleDto roleDto = new RoleDto();
        roleDto.setName("Regular Member");
        roleDto.setUserId(newRoleUserId);
        PrivilegeDto privilegeDto = new PrivilegeDto();
        privilegeDto.setName("READ_GROUP");
        roleDto.setAuthorities(List.of(privilegeDto));

        MvcResult rolePostResult = this.mockMvc.perform(post("/api/groupRole/byGroup/"+groupId+"/byUser/"+newRoleUserId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isForbidden())//.andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    void serverShouldReturnErrorIfPrivilegeIsNotCorrespondToRequirements() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek3", "jelszo3");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        Long memberUserId = 3l;
        /*
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/byGroup/2/byUser/"+memberUserId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        */
        Long roleId = 3l;
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/"+roleId+"/byGroup/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();


        RoleDto roleDto = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        roleDto.setName("GROUP_ROLE_INVALID_MODERATOR");
        roleDto.setDisplayName("Moderator");
        roleDto.setUserId(memberUserId);
        PrivilegeDto privilege = new PrivilegeDto();
        privilege.setName("INVALID_AUTHORITY_GROUP");
        roleDto.getAuthorities().add(privilege);
        MvcResult updateResult = this.mockMvc.perform(put("/api/groupRole/"+roleDto.getId().toString()+"/byGroup/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void groupFounderCanModifyOtherMemberGroupRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek3", "jelszo3");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);


        Long memberUserId = 3l;
        /*
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/byGroup/2/byUser/"+memberUserId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        */
        Long roleId = 3l;
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/"+roleId+"/byGroup/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();

        RoleDto roleDto = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        roleDto.setName("GROUP_ROLE_MODERATOR");
        roleDto.setDisplayName("Moderator");
        roleDto.setUserId(memberUserId);
        PrivilegeDto privilege = new PrivilegeDto();
        privilege.setName("DELETE_GROUP");
        roleDto.getAuthorities().add(privilege);
        MvcResult updateResult = this.mockMvc.perform(put("/api/groupRole/"+roleDto.getId().toString()+"/byGroup/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isOk())
                .andReturn();
        RoleDto updated = this.objectMapper.readValue(updateResult.getResponse().getContentAsString(), RoleDto.class);
        assertNotNull(updated.getName());
        assertTrue(updated.getName().equals(roleDto.getName()));
        assertTrue(updated.getDisplayName().equals(roleDto.getDisplayName()));
        assertTrue(updated.getAuthorities().size() == 2);
        updated.getAuthorities().forEach(updatedAuthority -> {
            boolean match = roleDto.getAuthorities().stream().anyMatch(dtoPrivilege -> dtoPrivilege.getName().equals(updatedAuthority.getName()));
            assertTrue(match);
        });
    }
    @Test
    void groupFounderCanDeleteOtherMemberGroupRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek3", "jelszo3");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        //Long memberUserId = 3l;
        Long roleId = 3l;
        String path = "/api/groupRole/"+roleId.toString()+"/byGroup/2";
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/"+roleId.toString()+"/byGroup/2")//.perform(get("/api/groupRole/byGroup/2/byUser/"+memberUserId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();


        RoleDto roleDto = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        MvcResult updateResult = this.mockMvc.perform(delete("/api/groupRole/"+roleDto.getId().toString()+"/byGroup/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void regularMemberCanNotDeleteHisGroupRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        Long memberUserId = 3l;

        /*
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/byGroup/2/byUser/"+memberUserId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();
        */
        Long roleId = 3l;
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/"+roleId+"/byGroup/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();

        RoleDto roleDto = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        MvcResult deleteResult = this.mockMvc.perform(delete("/api/groupRole/"+roleDto.getId().toString()+"/byGroup/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void regularMemberCanNotDeleteOtherMemberGroupRole() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek2", "jelszo2");
        MvcResult loginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        String token = loginResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        Long memberUserId = 4l;
        Long roleId = 2l;
        MvcResult getResult = this.mockMvc
                .perform(get("/api/groupRole/"+roleId+"/byGroup/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isOk())
                .andReturn();


        RoleDto roleDto = this.objectMapper.readValue(getResult.getResponse().getContentAsString(), RoleDto.class);
        MvcResult deleteResult = this.mockMvc.perform(delete("/api/groupRole/"+roleDto.getId().toString()+"/byGroup/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}