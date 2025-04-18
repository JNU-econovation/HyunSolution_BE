package com.hyunsolution.dangu.user.domain;

import com.hyunsolution.dangu.common.BaseEntity;
import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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
