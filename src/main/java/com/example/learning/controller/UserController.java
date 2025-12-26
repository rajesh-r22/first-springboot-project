package com.example.learning.controller;

import com.example.learning.dto.LoginRequest;
import com.example.learning.dto.JwtResponse;
import com.example.learning.entity.User;
import com.example.learning.service.JwtService;
import com.example.learning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public String registration(@RequestBody User user){
        userService.register(user);
        return "User registered successfully";
    }

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {

         Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );
         String token= jwtService.generateToken(request.getUsername());

        return new JwtResponse(token);
    }
}
