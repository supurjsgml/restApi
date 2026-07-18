package com.app.gemini.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.ApiBodyDTO;
import com.app.common.enums.MessageEnum;
import com.app.common.util.ApiResUtil;
import com.app.common.util.MessagesUtils;
import com.app.gemini.dto.req.GeminiReqDTO;
import com.app.gemini.dto.res.GeminiModelResDTO;
import com.app.gemini.dto.res.GeminiResDTO;
import com.app.gemini.service.GeminiService;
import com.app.gemini.service.RateLimitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "제미나이", description = "제미나이 AI 에이전트 연동 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;
    private final RateLimitService rateLimitService;
    private final WebClient webClient;

    // 인증 토큰 로컬 캐시 맵 및 만료시간(10분) 정의
    private final Map<String, CachedToken> tokenCache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 10 * 60 * 1000L;

    // 구글 Oauth 토큰 반환 결과 수신 DTO
    @lombok.Data
    @lombok.NoArgsConstructor
    private static class GoogleTokenInfo {
        private String user_id;
        private String sub;
        private String email;
        private String error;
    }

    // 캐싱 토큰 정보를 담기 위한 래퍼 DTO
    @Getter
    @RequiredArgsConstructor
    private static class CachedToken {
        private final GoogleTokenInfo tokenInfo;
        private final long createdAt = System.currentTimeMillis();

        public boolean isExpired(long durationMs) {
            return System.currentTimeMillis() - this.createdAt > durationMs;
        }
    }

    @ApiDocumentResponse
    @Operation(summary = "텍스트 요약 및 분석", description = "제미나이 API를 사용해 웹페이지 본문을 분석하고 가공합니다.", hidden = true)
    @PostMapping(value = "/summarize", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> summarize(
            @RequestBody GeminiReqDTO reqDTO,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Flux.just("ERROR: 인증 토큰이 누락되었습니다. 구글 로그인이 필요합니다.");
            }
            String token = authHeader.substring(7);

            // 캐시에서 토큰 조회 후 없거나 만료된 경우 구글 서버에서 재검증
            CachedToken cached = tokenCache.get(token);
            if (cached == null || cached.isExpired(CACHE_DURATION)) {
                GoogleTokenInfo tokenInfo = webClient.get()
                        .uri("https://oauth2.googleapis.com/tokeninfo?access_token=" + token)
                        .retrieve()
                        .bodyToMono(GoogleTokenInfo.class)
                        .onErrorReturn(new GoogleTokenInfo())
                        .block();

                if (tokenInfo != null && tokenInfo.getError() == null && 
                    (tokenInfo.getUser_id() != null || tokenInfo.getSub() != null || tokenInfo.getEmail() != null)) {
                    cached = new CachedToken(tokenInfo);
                    tokenCache.put(token, cached);
                } else {
                    cached = new CachedToken(tokenInfo != null ? tokenInfo : new GoogleTokenInfo());
                }
            }

            GoogleTokenInfo tokenInfo = cached.getTokenInfo();

            if (tokenInfo == null || tokenInfo.getError() != null || 
                (tokenInfo.getUser_id() == null && tokenInfo.getSub() == null && tokenInfo.getEmail() == null)) {
                return Flux.just("ERROR: 유효하지 않은 구글 인증 토큰입니다. 다시 로그인해 주세요.");
            }

            String email = tokenInfo.getEmail();
            String googleSubId = tokenInfo.getSub() != null ? tokenInfo.getSub() : tokenInfo.getUser_id();

            // 일일 한도 체크 (50회)
            Boolean isAllowed = rateLimitService.checkLimit(email, googleSubId).block();
            if (Boolean.FALSE.equals(isAllowed)) {
                return Flux.just("ERROR: 일일 요약 제한 횟수(50회)를 초과했습니다. 내일 다시 이용해 주세요.");
            }

            return geminiService.getSummary(reqDTO);
        } catch (Exception e) {
            log.error("GeminiController Exception ERROR : {}", e.getMessage(), e);
            return Flux.just("ERROR: " + MessagesUtils.getMessage(MessageEnum.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    @ApiDocumentResponse
    @Operation(summary = "제미나이 모델 목록 조회", description = "구글 Gemini API가 지원하는 텍스트 생성 모델 목록을 동적으로 가져옵니다.")
    @GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiBodyDTO.Response<List<GeminiModelResDTO>> getModels() {
        ApiBodyDTO.Response<List<GeminiModelResDTO>> response = null;
        try {
            List<GeminiModelResDTO> models = geminiService.getModelList().block();
            response = ApiResUtil.success(models, MessageEnum.SUCCESS.getCode());
        } catch (Exception e) {
            log.error("GeminiController.getModels Exception ERROR : {}", e.getMessage(), e);
            response = ApiResUtil.failed(MessagesUtils.getMessage(MessageEnum.INTERNAL_SERVER_ERROR.getCode()));
        }
        return response;
    }
}
