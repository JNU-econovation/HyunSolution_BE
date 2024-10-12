package com.hyunsolutiondemo.dangu.participant.domain;

import com.hyunsolutiondemo.dangu.user.domain.User;
import com.hyunsolutiondemo.dangu.workspace.domain.Workspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Workspace workspace;

    @Column(name = "participant_match", nullable = false)
    @ColumnDefault("false")
    private boolean participantMatch;

    @Builder
    public Participant(User user, Workspace workspace, boolean participantMatch) {
        this.user = user;
        this.workspace = workspace;
        this.participantMatch = participantMatch;
    }

    public void accept() {
        this.participantMatch = true;
    }

}
