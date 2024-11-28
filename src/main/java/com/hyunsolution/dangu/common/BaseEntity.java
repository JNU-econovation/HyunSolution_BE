package com.hyunsolution.dangu.common;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    //삭제
    private boolean isDeleted = false;

    public void toggleDeleted() {isDeleted = !isDeleted;}
}
