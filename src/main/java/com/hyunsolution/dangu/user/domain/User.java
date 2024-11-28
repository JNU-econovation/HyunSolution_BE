package com.hyunsolution.dangu.user.domain;

import javax.persistence.*;

import com.hyunsolution.dangu.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    @Column(name = "pwd", nullable = false)
    private String password;

    @Builder
    private User(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }
}
