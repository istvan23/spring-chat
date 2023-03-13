package com.example.springchatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

@SpringBootApplication
public class SpringChatServerApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(SpringChatServerApplication.class, args);
	}

}
