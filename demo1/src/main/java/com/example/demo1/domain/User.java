package com.example.demo1.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`user`")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "loginId", length = 50, unique = true)
    private String loginId;

    @Column(name = "password", length =100)
    private String password;

    @Column(name = "authorize")
    private String authorize;

    @Builder
    public User(String username, String loginId, String password, String authorize) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.authorize = authorize;
    }
}
