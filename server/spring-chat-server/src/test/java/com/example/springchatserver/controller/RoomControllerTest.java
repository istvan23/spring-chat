package com.example.springchatserver.controller;

import com.example.springchatserver.util.TestDbInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomControllerTest {
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
}