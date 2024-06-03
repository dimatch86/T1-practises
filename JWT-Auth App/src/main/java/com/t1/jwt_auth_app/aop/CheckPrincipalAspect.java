package com.t1.jwt_auth_app.aop;

import com.t1.jwt_auth_app.exception.SelfModificationException;
import com.t1.jwt_auth_app.security.AppUserDetails;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class CheckPrincipalAspect {

    @Pointcut("execution(* com.t1.jwt_auth_app.service.UserService.deleteUser(Long))")
    public void checkPrincipalPointcut(){}

    @Before("args(id) && checkPrincipalPointcut()")
    public void checkPrincipalAdvice(Long id) {
        AppUserDetails currentUser = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.equals(id, currentUser.getId())) {
            throw new SelfModificationException("Вы не можете удалить себя");
        }
    }
}
