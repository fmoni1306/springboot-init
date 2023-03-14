package com.example.springbootinit.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDTO {

    private String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
