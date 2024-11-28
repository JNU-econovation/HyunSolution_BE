package com.hyunsolution.dangu.workspace.domain;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.common.BaseEntity;
import com.hyunsolution.dangu.user.domain.User;
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
public class Workspace extends BaseEntity {

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

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @Builder
    private Workspace(User creator, boolean isMatched, int totalCnt) {
        this.creator = creator;
    }

    public void acceptMatchingFinal() {
        this.isMatched = true;
    }
}
