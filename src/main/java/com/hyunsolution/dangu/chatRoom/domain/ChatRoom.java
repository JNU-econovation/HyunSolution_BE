package com.hyunsolution.dangu.chatRoom.domain;

import com.hyunsolution.dangu.participant.domain.Participant;
import com.hyunsolution.dangu.workspace.domain.Workspace;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workspace_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Workspace workspace;

    @Column(name = "is_matched", nullable = false)
    @ColumnDefault("false")
    private boolean isMatched;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "ch_update_at")
    private LocalDateTime chatUpdateAt;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

    @Builder
    private ChatRoom(Workspace workspace, boolean isMatched) {
        this.workspace = workspace;
        this.isMatched = isMatched;
    }

    public void acceptMatching() {
        this.isMatched = true;
    }
}
