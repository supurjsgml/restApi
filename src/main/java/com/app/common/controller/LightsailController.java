package com.app.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/lightsail")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class LightsailController {

    @GetMapping("/restart")
    public HttpStatus restartScheduler() {
        log.info("라이트세일 PM2 프로세스(scheduler-job) 재시작 요청 수신");
        try {
            // 리눅스 환경에서 pm2 restart 명령을 자식 프로세스로 구동
            String[] cmd = {"/bin/sh", "-c", "pm2 restart scheduler-job"};
            Runtime.getRuntime().exec(cmd);
            log.info("PM2 재시작 명령 전송 완료");
        } catch (Exception e) {
            log.error("PM2 프로세스 재시작 실패: {}", e.getMessage());
        }
        return HttpStatus.OK;
    }

    @GetMapping("/restart-api")
    public HttpStatus restartRestApi() {
        log.info("라이트세일 API 서버 무중단 재기동 요청 수신");
        try {
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", "/home/ubuntu/rest-api/deploy.sh");
            pb.directory(new java.io.File("/home/ubuntu/rest-api"));
            
            // 현재 JVM 프로세스가 상속받은 모든 환경변수(REDIS_*, KAKAO_* 등)를 자식 프로세스 환경변수로 그대로 상속
            pb.environment().putAll(System.getenv());
            pb.environment().putIfAbsent("PORT_A", "8080");
            pb.environment().putIfAbsent("PORT_B", "9080");
            
            pb.start();
            log.info("무중단 재기동 스크립트(deploy.sh) 실행 시작 완료");
        } catch (Exception e) {
            log.error("무중단 재기동 스크립트 실행 실패: {}", e.getMessage());
        }
        return HttpStatus.OK;
    }
}
