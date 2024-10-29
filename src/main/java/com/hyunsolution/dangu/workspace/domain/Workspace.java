package com.hyunsolution.dangu.workspace.domain;

import com.hyunsolution.dangu.user.domain.User;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User creator;

    @Column(name = "is_matched", nullable = false)
    @ColumnDefault("false")
    private boolean isMatched;

    @Column(name = "total_cnt", nullable = false)
    @ColumnDefault("0")
    private int totalCnt;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Workspace(User creator, boolean isMatched, int totalCnt, LocalDateTime createdAt) {
        this.creator = creator;
        this.isMatched = isMatched;
        this.totalCnt = totalCnt;
        this.createdAt = createdAt;
    }

    public void acceptFinal() {
        this.isMatched = true;
    }
}
