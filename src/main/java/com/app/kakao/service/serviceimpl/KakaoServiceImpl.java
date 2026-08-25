package com.app.kakao.service.serviceimpl;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
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

    private static final String KEY_KAKAO_REFRESH_TOKEN = "kakao:token:refresh";
    private static final String KEY_KAKAO_ACCESS_TOKEN = "kakao:token:access";

    private static final ObjectMapper objectMapper = CommonUtil.om;

    private final WebClientUtil webClientUtil;
    private final RestApiProperties restApiProperties;
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Value("${key.kakao.clientId}")
    private String clientId;

    @Value("${key.kakao.clientSecret}")
    private String clientSecret;

    @Value("${key.kakao.refreshToken}")
    private String defaultRefreshToken;

    private Mono<String> getRefreshToken() {
        return reactiveStringRedisTemplate.opsForValue().get(KEY_KAKAO_REFRESH_TOKEN)
                .filter(token -> !token.trim().isEmpty())
                .switchIfEmpty(Mono.defer(() -> {
                    if (defaultRefreshToken != null && !defaultRefreshToken.trim().isEmpty()) {
                        log.info("설정 파일의 카카오 리프레시 토큰을 Redis에 초기 등록합니다.");
                        return reactiveStringRedisTemplate.opsForValue()
                                .set(KEY_KAKAO_REFRESH_TOKEN, defaultRefreshToken.trim(), Duration.ofDays(60))
                                .thenReturn(defaultRefreshToken.trim());
                    }
                    return Mono.empty();
                }));
    }

    public Mono<String> getAccessToken() {
        return reactiveStringRedisTemplate.opsForValue().get(KEY_KAKAO_ACCESS_TOKEN)
                .filter(token -> !token.trim().isEmpty())
                .switchIfEmpty(Mono.defer(() -> refreshKakaoToken().map(KakaoTokenResDTO::getAccessToken)));
    }

    @Override
    public Mono<KakaoTokenResDTO> refreshKakaoToken() {
        return getRefreshToken()
                .switchIfEmpty(Mono.error(new IllegalStateException("카카오 리프레시 토큰이 존재하지 않습니다.")))
                .flatMap(currentRefreshToken -> {
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
                        String newAccessToken = (String) response.get("access_token");
                        Number expiresInNum = (Number) response.get("expires_in");
                        long expiresIn = expiresInNum != null ? expiresInNum.longValue() : 21600L;
                        long accessTtl = expiresIn > 300 ? expiresIn - 300 : expiresIn;

                        String newRefreshToken = (String) response.get("refresh_token");
                        Number refreshExpiresInNum = (Number) response.get("refresh_token_expires_in");
                        long refreshExpiresIn = refreshExpiresInNum != null ? refreshExpiresInNum.longValue() : 5184000L;

                        Mono<Boolean> saveAccessMono = (newAccessToken != null && !newAccessToken.isEmpty())
                                ? reactiveStringRedisTemplate.opsForValue().set(KEY_KAKAO_ACCESS_TOKEN, newAccessToken, Duration.ofSeconds(accessTtl))
                                : Mono.just(false);

                        Mono<Boolean> saveRefreshMono;
                        if (newRefreshToken != null && !newRefreshToken.trim().isEmpty()) {
                            log.info("새로운 카카오 리프레시 토큰이 발급되어 Redis에 갱신 저장합니다.");
                            saveRefreshMono = reactiveStringRedisTemplate.opsForValue().set(KEY_KAKAO_REFRESH_TOKEN, newRefreshToken.trim(), Duration.ofSeconds(refreshExpiresIn));
                        } else {
                            saveRefreshMono = Mono.just(true);
                        }

                        return Mono.zip(saveAccessMono, saveRefreshMono)
                                .map(tuple -> KakaoTokenResDTO.builder()
                                        .accessToken(newAccessToken)
                                        .newRefreshToken(newRefreshToken)
                                        .build());
                    });
                })
                .doOnSuccess(dto -> log.info("카카오 토큰 Redis 갱신 성공"))
                .doOnError(e -> log.error("카카오 토큰 갱신 실패: {}", e.getMessage()));
    }

    private Mono<String> sendKakaoInternal(String msg, String restartPath) {
        return getAccessToken().flatMap(accessToken -> {
            try {
                KakaoTextTemplate template = (restartPath != null && !restartPath.isEmpty())
                        ? KakaoTextTemplate.restartTemplate(msg, restApiProperties.getBaseUrl(), restartPath)
                        : KakaoTextTemplate.restartTemplate(msg, restApiProperties.getBaseUrl());

                String templateJson = objectMapper.writeValueAsString(template);
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("template_object", templateJson);

                return webClientUtil.postFormAsync(
                        restApiProperties.getKakao().getApi().getMemo(),
                        formData,
                        Map.of("Authorization", "Bearer ".concat(accessToken)),
                        String.class
                );
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        });
    }

    @Override
    public void sendKakao(String msg) {
        sendKakaoInternal(msg, "/api/lightsail/restart-api")
                .subscribe(
                        res -> log.info("카카오 알림 전송 성공 : {}", res),
                        err -> log.error("카카오 알림 전송 최종 실패: {}", err.getMessage())
                );
    }

    @Override
    public Mono<KakaoTokenResDTO> sendKakaoExternal(String msg) {
        return sendKakaoInternal(msg, null)
                .map(res -> KakaoTokenResDTO.builder().build())
                .doOnError(e -> log.error("외부 카카오 메시지 전송 실패: {}", e.getMessage()));
    }

}

