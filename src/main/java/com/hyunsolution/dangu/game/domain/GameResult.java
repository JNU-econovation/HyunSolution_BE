package com.hyunsolution.dangu.game.domain;

import com.hyunsolution.dangu.user.domain.User;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class GameResult {
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

    @ColumnDefault("false")
    private Boolean winner;

    @Column(name = "start_score")
    private Integer startScore;

    @Column(name = "final_score")
    private Integer finalScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "game_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Game game;

    @Builder
    private GameResult(User user, Integer startScore, Integer finalScore, Game game) {
        this.user = user;
        this.startScore = startScore;
        this.finalScore = finalScore;
        this.game = game;
    }

    public double calculateWin() {
        return (double) this.getFinalScore() / this.getStartScore();
    }
}
