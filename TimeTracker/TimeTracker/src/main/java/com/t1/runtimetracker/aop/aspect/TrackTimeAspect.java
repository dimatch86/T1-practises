package com.t1.runtimetracker.aop.aspect;

import com.t1.runtimetracker.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("tracker.enable")
@Aspect
@RequiredArgsConstructor
public class TrackTimeAspect {
    private final TrackService trackService;

    @Pointcut("@annotation(com.t1.runtimetracker.aop.annotation.TrackTime)")
    public void syncRunnerPointcut(){}

    @Around("syncRunnerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        return trackService.proceedAndSaveTrack(joinPoint, Boolean.FALSE);
    }
}
