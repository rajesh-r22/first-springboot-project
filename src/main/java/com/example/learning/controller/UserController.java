package com.example.learning.controller;

import com.example.learning.dtoRequest.RefreshRequest;
import com.example.learning.dtoResponse.JwtResponse;
import com.example.learning.dtoResponse.LoginRequest;
import com.example.learning.entity.RefreshToken;
import com.example.learning.entity.User;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.JwtService;
import com.example.learning.service.RefreshTokenService;
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
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public String registration(@RequestBody User user){
        userService.register(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {

//        authenticate karo username + password ko
         Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

//        user ko load karo dataBase se
         User user=userRepository.findByUsername(request.getUsername())
                 .orElseThrow(()->new RuntimeException("User not found"));

//         access token generate karo
         String accessToken= jwtService.generateToken(request.getUsername());

//         refresh token generate karo aur DB me save karo token ko
         RefreshToken refreshToken=refreshTokenService.createRefreshToken(user.getId());

//         access token + refresh token return karo client ko
      return new JwtResponse(
              accessToken,
              refreshToken.getToken()
      );
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshRequest request){
//        client refresh token dta hai hume , hum use verify kar te hai
        RefreshToken rt=refreshTokenService.verifyExpiration(request.getRefreshToken());

//        verify success -> new access token generate kar ke dio
        String newAccessToken = jwtService.generateToken(rt.getUser().getUsername());
        return new JwtResponse(newAccessToken,rt.getToken());
    }
}
