package com.devjaewoo.openroadmaps.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
public class RequestLoggingAop {

    @Around("execution(* com.devjaewoo.openroadmaps..*Controller.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        try {
            Object result = joinPoint.proceed();
            if(request != null) {
                log.info("Request URI: {}, Time elapsed: {}ms", request.getRequest().getRequestURI(), System.currentTimeMillis() - start);
            }

            return result;
        }
        catch (Throwable e) {
            if(request != null) {
                log.info("Request URI: {}, Time elapsed: {}ms, Exception: {}", request.getRequest().getRequestURI(), System.currentTimeMillis() - start, e.getMessage());
            }

            throw e;
        }
    }
}
