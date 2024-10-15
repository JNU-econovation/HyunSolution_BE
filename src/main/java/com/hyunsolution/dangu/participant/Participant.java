<<<<<<<< HEAD:src/main/java/com/hyunsolution/dangu/participant/Participant.java
package com.hyunsolution.dangu.participant;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import javax.persistence.*;
========
package com.hyunsolution.dangu.participant.domain;

import com.hyunsolution.dangu.user.domain.User;
import com.hyunsolution.dangu.workspace.domain.Workspace;
>>>>>>>> e26f9a76aca76b518f2e10421cc962bf59c57d48:src/main/java/com/hyunsolution/dangu/participant/domain/Participant.java
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
            name = "workspace_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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
<<<<<<<< HEAD:src/main/java/com/hyunsolution/dangu/participant/Participant.java
========

    public void accept() {
        this.participantMatch = true;
    }

>>>>>>>> e26f9a76aca76b518f2e10421cc962bf59c57d48:src/main/java/com/hyunsolution/dangu/participant/domain/Participant.java
}
