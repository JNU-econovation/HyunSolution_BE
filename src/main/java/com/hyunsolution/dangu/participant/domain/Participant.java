package com.hyunsolution.dangu.participant.domain;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.user.domain.User;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "chatRoom_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ChatRoom chatRoom;

    @Column(name = "participant_match", nullable = false)
    @ColumnDefault("false")
    private boolean participantMatch;

    @Column(name = "game_attend", nullable = false)
    @ColumnDefault("false")
    private boolean gameAttend;

    @Builder
    private Participant(
            User user, ChatRoom chatRoom, boolean participantMatch, boolean gameAttend) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.participantMatch = participantMatch;
        this.gameAttend = gameAttend;
    }

    public void updateParticipantMatch(boolean isMatch) {
        this.participantMatch = isMatch;
    }
}
