package com.example.springbootinit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String name;

    @Column(length = 20)
    private String password;

    @Builder
    public User(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
