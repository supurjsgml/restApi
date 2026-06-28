package com.app.kakao.service.serviceimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.app.common.constants.RestApiProperties;
import com.app.common.util.CommonUtil;
import com.app.common.util.WebClientUtil;
import com.app.kakao.dto.KakaoTokenResDTO;
import com.app.kakao.service.KakaoService;
import com.app.kakao.template.KakaoTextTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private static final ObjectMapper objectMapper = CommonUtil.om;

    private final WebClientUtil webClientUtil;

    private final RestApiProperties restApiProperties;

    // 카카오 앱 인증 정보 (Scheduler-Job이 refreshToken을 넘겨주므로, clientId/clientSecret만 restApi에서 보유)
    @Value("${key.kakao.clientId}")
    private String clientId;

    @Value("${key.kakao.clientSecret}")
    private String clientSecret;

    @Value("${key.kakao.refreshToken}")
    private String refreshToken;

    private String loadRefreshToken() {
        try {
            java.io.File file = new java.io.File("kakao-token.txt");
            if (file.exists()) {
                String fileToken = java.nio.file.Files.readString(file.toPath()).trim();
                if (!fileToken.isEmpty()) {
                    log.info("리프레시 토큰을 로드.");
                    return fileToken;
                }
            }
        } catch (Exception e) {
            log.error("loadRefreshToken ERROR : {}", e.getMessage());
        }
        log.info("설정 정보의 리프레시 토큰을 사용.");
        return refreshToken;
    }

    private void saveRefreshToken(String newToken) {
        try {
            java.io.File file = new java.io.File("kakao-token.txt");
            java.nio.file.Files.writeString(file.toPath(), newToken.trim());
            log.info("리프레시 토큰 저장");
        } catch (Exception e) {
            log.error("리프레시 토큰 저장중 오류 발생: {}", e.getMessage());
        }
    }

    public Mono<String> getAccessToken() {
        String currentRefreshToken = loadRefreshToken();
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", currentRefreshToken);

        return webClientUtil.postFormAsync(
                restApiProperties.getKakao().getAuth().getToken(),
                formData,
                null,
                Map.class
        ).flatMap(response -> {
            String accessToken = (String) response.get("access_token");
            String newRefreshToken = (String) response.get("refresh_token");
            if (newRefreshToken != null && !newRefreshToken.trim().isEmpty()
                    && !newRefreshToken.equals(currentRefreshToken)) {
                saveRefreshToken(newRefreshToken);
            }
            return Mono.just(accessToken);
        }).doOnError(e -> log.error("카카오 토큰 갱신 실패: {}", e.getMessage()));
    }

    @Override
    public void sendKakao(String msg) {
        getAccessToken().flatMap(accessToken -> {
            try {
                // 카톡text 템플릿 생성
                KakaoTextTemplate template = KakaoTextTemplate.restartTemplate(msg,
                        restApiProperties.getBatch().getBaseUrl(),
                        "/api/lightsail/restart-api");

                // 제이슨 ~ 직여을화
                String templateJson = objectMapper.writeValueAsString(template);

                // 파람이 태어났어요
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("template_object", templateJson);

                // 카톡 나에게 메세지 전송
                return webClientUtil.postFormAsync(
                        restApiProperties.getKakao().getApi().getMemo(),
                        formData,
                        Map.of("Authorization", "Bearer ".concat(accessToken)),
                        String.class
                );
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        })
        .subscribe(
                res -> log.info("카카오 알림 전송 성공 : {}", res),
                err -> log.error("카카오 알림 전송 최종 실패: {}", err.getMessage()));
    }

    @Override
    public Mono<KakaoTokenResDTO> refreshTokenExternal(String externalRefreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", externalRefreshToken);

        return webClientUtil.postFormAsync(
                restApiProperties.getKakao().getAuth().getToken(),
                formData,
                null,
                Map.class
        ).map(response -> {
            String newRefreshToken = (String) response.get("refresh_token");
            boolean hasNew = newRefreshToken != null && !newRefreshToken.trim().isEmpty()
                    && !newRefreshToken.equals(externalRefreshToken);
            if (hasNew) log.info("새 refreshToken 반환 (Scheduler-Job에서 저장 예정)");
            return KakaoTokenResDTO.builder()
                    .accessToken((String) response.get("access_token"))
                    .newRefreshToken(hasNew ? newRefreshToken : null)
                    .build();
        }).doOnError(e -> log.error("외부 카카오 토큰 갱신 실패: {}", e.getMessage()));
    }

    @Override
    public Mono<KakaoTokenResDTO> sendKakaoExternal(String msg, String externalRefreshToken) {
        return refreshTokenExternal(externalRefreshToken)
                .flatMap(tokenResult -> {
                    String accessToken = tokenResult.getAccessToken();
                    String newRefreshToken = tokenResult.getNewRefreshToken();

                    KakaoTextTemplate template = KakaoTextTemplate.restartTemplate(msg,
                            restApiProperties.getBatch().getBaseUrl());

                    String templateJson;
                    try {
                        templateJson = objectMapper.writeValueAsString(template);
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }

                    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                    formData.add("template_object", templateJson);

                    return webClientUtil.postFormAsync(
                            restApiProperties.getKakao().getApi().getMemo(),
                            formData,
                            Map.of("Authorization", "Bearer ".concat(accessToken)),
                            String.class
                    ).map(res -> KakaoTokenResDTO.builder()
                            .newRefreshToken(newRefreshToken)
                            .build());
                })
                .doOnError(e -> log.error("외부 카카오 메시지 전송 실패: {}", e.getMessage()));
    }

}
