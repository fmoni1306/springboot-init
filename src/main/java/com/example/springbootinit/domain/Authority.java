package com.example.springbootinit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @Column
    private String authorityName;
    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    private List<Member> member;
    @Builder
    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
