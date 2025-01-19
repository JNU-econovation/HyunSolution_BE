package com.hyunsolution.dangu.game.domain;

import com.hyunsolution.dangu.common.BaseEntity;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
            name = "workspace_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Workspace workspace;

    @Column(name = "game_round", nullable = false)
    @ColumnDefault("1")
    private Integer gameRound;

    @Column(name = "start_time", nullable = false)
    @CreatedDate
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "tableNumber")
    private Integer tableNumber;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<GameResult> gameResults = new ArrayList<>();

    private Game(Workspace workspace) {
        this.workspace = workspace;
        this.gameRound = 1;
    }

    private Game(Integer gameRound, Workspace workspace) {
        this.workspace = workspace;
        this.gameRound = gameRound;
    }

    public static Game createDefaultGame(Workspace workspace) {
        return new Game(workspace);
    }

    public static Game createGameWithRound(Integer gameRound, Workspace workspace) {
        return new Game(gameRound, workspace);
    }

    public long getGameTime() {
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    public long calculateCost() {
        long gameTime = getGameTime();
        if (gameTime <= 30) {
            return 2200 * 3;
        }
        return (getGameTime() / 10) * 2200;
    }
}
