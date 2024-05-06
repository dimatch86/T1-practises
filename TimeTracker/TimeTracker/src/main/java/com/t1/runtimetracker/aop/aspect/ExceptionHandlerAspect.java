package com.t1.runtimetracker.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ExceptionHandlerAspect {

    @AfterThrowing(pointcut = "within(com.t1.runtimetracker.service.*) && " +
            "execution(* * (..) throws @com.t1.runtimetracker.aop.annotation.Throw *)", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        log.info("Произошла ошибка в методе {}", joinPoint.getSignature().toShortString());
        log.info("Exception {}", exception.getLocalizedMessage());
    }
}
