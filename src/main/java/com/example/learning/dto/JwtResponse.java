package com.example.learning.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private String response;

    public JwtResponse(String response) {
        this.response=response;
    }
}
