package com.example.learning.service;

import com.example.learning.entity.RefreshToken;
import com.example.learning.entity.User;
import com.example.learning.repository.RefreshTokenRepository;
import com.example.learning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.refresh-expiration}")
     private long refreshExpiration;

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(Long userid){

        User user= userRepository.findById(userid)
                .orElseThrow(()-> new RuntimeException("user not found"));
        refreshTokenRepository.deleteByUser(user);

        RefreshToken token=new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshExpiration));

        return  refreshTokenRepository.save(token);

    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired!");
        }
        return token;
    }
}
