package com.hyunsolution.dangu.chatRoom.domain;

import com.hyunsolution.dangu.workspace.domain.Workspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)//존재 이유 찾아보기
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
    private boolean isMatched;

    @Column(name = "created_at",nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "ch_update_at")
    private LocalDateTime chatUpdateAt;

    @Builder
    private ChatRoom(Workspace workspace, boolean isMatched) {
        this.workspace = workspace;
        this.isMatched = isMatched;
    }

    public void acceptFinal() {
        this.isMatched = true;
    }

}
