package com.hyunsolution.dangu.chatlog.domain;

import com.hyunsolution.dangu.chatRoom.domain.ChatRoom;
import com.hyunsolution.dangu.user.domain.User;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatLog {

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
            name = "chatRoom_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ChatRoom chatRoom;

    @Column(name = "enter_time", nullable = true)
    private LocalDateTime enterTime;

    @Column(name = "is_out", nullable = false)
    @ColumnDefault("false")
    private Boolean isOut;

    @Column(name = "read_count", nullable = false)
    @ColumnDefault("0")
    private Integer readCount;

    /*
    public void updateReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public void read() {
        this.readCount++;
    }

     */

    @Builder
    private ChatLog(User user, ChatRoom chatRoom, LocalDateTime enterTime) {
        this.user = user;
        this.chatRoom = chatRoom;
        this.enterTime = enterTime;
        this.isOut = false;
        this.readCount = 0;
    }
}
