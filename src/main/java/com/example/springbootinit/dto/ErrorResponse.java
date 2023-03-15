package com.example.springbootinit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int status;

    private String message;

    private String code;

    @Builder
    public ErrorResponse(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
