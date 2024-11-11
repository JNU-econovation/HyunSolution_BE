package com.hyunsolution.dangu.common.event;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class EventPublisherAspect implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    private ThreadLocal<Boolean> appliedLocal = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Around("@annotation(com.hyunsolution.dangu.common.event.EventPublish)")
    public Object handleEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean nested = appliedLocal.get();

        if (!nested) {
            appliedLocal.set(Boolean.TRUE);
            Events.setPublisher(publisher);
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (!nested) {
                Events.reset();
                appliedLocal.remove();
            }
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.publisher = eventPublisher;
    }
}
