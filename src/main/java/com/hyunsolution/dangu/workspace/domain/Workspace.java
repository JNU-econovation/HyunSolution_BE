package com.hyunsolution.dangu.workspace.domain;

import com.hyunsolution.dangu.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "is_matched", nullable = false)
    @ColumnDefault("false")
    private boolean isMatched;

    @Column(name = "total_cnt", nullable = false)
    @ColumnDefault("0")
    private int totalCnt;

    @Builder
    public Workspace(User user, boolean isMatched, int totalCnt) {
        this.user=user;
        this.isMatched = isMatched;
        this.totalCnt=totalCnt;
    }



}
