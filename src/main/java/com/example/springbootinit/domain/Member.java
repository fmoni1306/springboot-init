package com.example.springbootinit.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true)
    private String name;

    @Column(length = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority", nullable = false)
    private Authority authority;

    @Column
    private Boolean isActivated;

    @Builder
    public Member(Long id, String name, String password, Authority authority, boolean isActivated) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authority = authority;
        this.isActivated = isActivated;
    }
}
