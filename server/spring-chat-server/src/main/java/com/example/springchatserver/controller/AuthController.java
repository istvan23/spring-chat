package com.example.springchatserver.controller;

import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.LoginRequest;
import com.example.springchatserver.dto.UserDto;
import com.example.springchatserver.mapper.UserMapper;
import com.example.springchatserver.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request){//(Authentication authentication){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDto user = userMapper.convertToDto((User) authentication.getPrincipal());

            Instant now = Instant.now();
            long expiry = 36000L;
            // @formatter:off
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .build();

            String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            //TODO: REFRESH TOKEN
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(user);
        }
        catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*@PostMapping("/registration")
    public ResponseEntity<?> registration(UserDto userDto){
        this.userService.registerNewUser(userDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }*/
}
