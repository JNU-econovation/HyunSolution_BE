package com.hyunsolution.dangu.common;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    // 삭제
    private boolean isDeleted;

    public void toggleDeleted() {
        isDeleted = !isDeleted;
    }
}
