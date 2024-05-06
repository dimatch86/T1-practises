package com.t1.runtimetracker.aop.aspect;

import com.t1.runtimetracker.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@ConditionalOnProperty("tracker.enable")
@Aspect
@RequiredArgsConstructor
public class TrackAsyncAspect {
    private final TrackService trackService;

    @Pointcut(value = "@annotation(com.t1.runtimetracker.aop.annotation.TrackAsyncTime)")
    public void asyncRunnerPointcut(){}

    @Around("asyncRunnerPointcut()")
    public Object asyncRunnerAdvice(ProceedingJoinPoint joinPoint) {
        return CompletableFuture.runAsync(() ->
                trackService.proceedAndSaveTrack(joinPoint, Boolean.TRUE));
    }
}
