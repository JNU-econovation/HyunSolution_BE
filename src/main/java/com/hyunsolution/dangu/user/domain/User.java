package com.hyunsolution.dangu.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uid;

    @Column(name = "pwd", nullable = false)
    private String password;

    @Builder
    public User(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }

}
