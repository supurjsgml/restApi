package com.app.kakao.service;

import com.app.kakao.dto.KakaoTokenResDTO;

import reactor.core.publisher.Mono;

public interface KakaoService {

    /**
     * 카카오 메시지 전송 (자체 토큰 사용)
     */
    void sendKakao(String msg);

    /**
     * [Scheduler-Job 전용]
     * Scheduler-Job이 보내온 refreshToken으로 카카오 토큰 갱신
     */
    Mono<KakaoTokenResDTO> refreshTokenExternal(String externalRefreshToken);

    /**
     * [Scheduler-Job 전용]
     * Scheduler-Job이 보내온 refreshToken + msg로 카카오 메시지 전송
     * 새 refreshToken이 있으면 newRefreshToken 포함 반환
     */
    Mono<KakaoTokenResDTO> sendKakaoExternal(String msg, String externalRefreshToken);

}
