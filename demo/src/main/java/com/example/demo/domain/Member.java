package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
//@Builder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Builder
    public Member(String name, String loginId, String password, Set<Authority> authorities) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }
}
