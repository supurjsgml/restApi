package com.app.common.core;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class RequestLoggingAspect {

    private static final ThreadLocal<String> uuidThreadLocal = new ThreadLocal<>();
//    com.app.common.dto.req /restApi/src/main/java/com/app/common/dto/req/FileGenReqDTO.java
    @Before("execution(* com.app.*.controller.*.*(..))")
    public void logRequest(JoinPoint joinPoint) {
        String uuid = UUID.randomUUID().toString();
        uuidThreadLocal.set(uuid); // UUID를 스레드 로컬 변수에 저장
        
        // HttpServletRequest 객체를 사용하여 요청 URL 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURL = request.getRequestURL().toString();

        log.info("Request UUID : {} - URL : {} - Package Info : {}", uuid, requestURL, joinPoint.getSignature());
    }

    public static String getCurrentUUID() {
        return uuidThreadLocal.get(); // 스레드 로컬 변수에서 UUID를 가져옴
    }
    
}
