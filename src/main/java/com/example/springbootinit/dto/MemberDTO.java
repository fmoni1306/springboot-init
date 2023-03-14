package com.example.springbootinit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDTO {

    @NotNull
    private String name;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String authority;

    @Builder

    public MemberDTO(String name, String password, String authority) {
        this.name = name;
        this.password = password;
        this.authority = authority;
    }
}
