package com.hyunsolutiondemo.dangu.chatlog;

import com.hyunsolutiondemo.dangu.user.User;
import com.hyunsolutiondemo.dangu.workspace.Workspace;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Workspace workspace;

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
    public Chatlog( User user, Workspace workspace, LocalDateTime enterTime,Boolean isOut,Integer readCount){
        this.user = user;
        this.workspace = workspace;
        this.enterTime=enterTime;
        this.isOut = isOut;
        this.readCount = readCount;
    }


}
