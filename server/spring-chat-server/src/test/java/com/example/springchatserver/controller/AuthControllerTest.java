package com.example.springchatserver.controller;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.LoginRequest;
import com.example.springchatserver.dto.UserDto;
import com.example.springchatserver.util.TestDbInitializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.coyote.http11.upgrade.UpgradeServletOutputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDbInitializer testDbInitializer;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @BeforeAll
    void setup(){
        testDbInitializer.initDbWithPreConstructedUserSamples();
    }

    @Test
    void loginSuccessWithAPreInitializedUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "jelszo1");
        MvcResult result = this.mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        System.out.println("----------------------------------------------");
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getHeader(HttpHeaders.AUTHORIZATION));

    }
    @Test
    void shouldCreateUserWithCorrectCredentials() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setUsername("ujuser");
        newUser.setEmail("ujuser@mail.com");
        newUser.setPassword("p4ssw0rd");
        MvcResult registrationResult = this.mockMvc.perform(post("/api/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andReturn();
        LoginRequest loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
        MvcResult tryLoginResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        UserDto returnedUserDto = this.objectMapper.readValue(tryLoginResult.getResponse().getContentAsString(), UserDto.class);
        assertTrue(returnedUserDto.getId() != null);
        assertEquals(returnedUserDto.getUsername(), newUser.getUsername());
        assertTrue(returnedUserDto.getPassword() != null && returnedUserDto.getPassword() != "");
        assertEquals(returnedUserDto.getEmail(), newUser.getEmail());

    }

    @Test
    void shouldDenyAccessIfThePasswordIsWrong() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tesztelek1", "nemjelszo1");
        MvcResult result = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();
    }


}