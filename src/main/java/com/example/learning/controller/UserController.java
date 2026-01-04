package com.example.learning.controller;

import com.example.learning.dto.AuthResponse;
import com.example.learning.dto.LoginRequest;
import com.example.learning.entity.RefreshToken;
import com.example.learning.entity.User;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.JwtService;
import com.example.learning.service.RefreshTokenService;
import com.example.learning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public String registration(@RequestBody User user){
        userService.register(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

         Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

         User user=userRepository.findByUsername(request.getUsername())
                 .orElseThrow(()->new RuntimeException("User not found"));

         String accessToken= jwtService.generateToken(request.getUsername());

         RefreshToken refreshToken=refreshTokenService.createRefreshToken(user.getId());

      return new AuthResponse(
              accessToken,
              refreshToken.getToken()
      );
    }
}
