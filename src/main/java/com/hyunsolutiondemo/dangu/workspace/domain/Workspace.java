package com.hyunsolutiondemo.dangu.workspace.domain;

import com.hyunsolutiondemo.dangu.user.domain.User;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "is_matched", nullable = false)
    @ColumnDefault("false")
    private boolean isMatched;

    @Column(name = "total_cnt", nullable = false)
    @ColumnDefault("0")
    private Integer totalCnt;

    @Builder
    public Workspace(User user, boolean isMatched,Integer totalCnt) {
        this.user=user;
        this.isMatched = isMatched;
        this.totalCnt=totalCnt;
    }

    public void finalAccept() {
        this.isMatched = true;
    }


}
