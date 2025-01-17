package com.hyunsolution.dangu.game.domain;

import com.hyunsolution.dangu.common.BaseEntity;
import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workspace_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Workspace workspace;

    @Column(name = "game_round", nullable = false)
    @ColumnDefault("1")
    private Integer gameRound;

    @ColumnDefault("false")
    private Boolean winner;

    @Column(name = "start_score")
    private Integer startScore;

    @Column(name = "final_score")
    private Integer finalScore;

    @Column(name = "start_time", nullable = false)
    @CreatedDate
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Builder
    private Game(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
        this.gameRound = 1;
        this.winner = false;
    }
}
