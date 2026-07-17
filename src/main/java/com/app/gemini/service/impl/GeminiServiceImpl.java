package com.app.gemini.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.app.common.constants.RestApiProperties;
import com.app.gemini.dto.GeminiApiRequest;
import com.app.gemini.dto.GeminiApiResponse;
import com.app.gemini.dto.GoogleModelListResponse;
import com.app.gemini.dto.req.GeminiReqDTO;
import com.app.gemini.dto.res.GeminiModelResDTO;
import com.app.gemini.service.GeminiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService {

    private final WebClient webClient;
    private final RestApiProperties restApiProperties;

    @Value("${key.gemini.apiKey}")
    private String apiKey;

    @Override
    public Mono<String> getSummary(GeminiReqDTO reqDTO) {
        String model = reqDTO.getModel();
        if (model == null || model.trim().isEmpty()) {
            model = "gemini-1.5-flash";
        }

        String url = restApiProperties.getGemini().getGenerateContent().replace("{model}", model) + "?key=" + apiKey;

        String combinedPrompt = reqDTO.getPrompt() + "\n\n[웹페이지 본문 내용]\n" + reqDTO.getText();

        GeminiApiRequest requestPayload = GeminiApiRequest.builder()
                .contents(List.of(
                        GeminiApiRequest.Content.builder()
                                .parts(List.of(
                                        GeminiApiRequest.Part.builder()
                                                .text(combinedPrompt)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        return webClient.post()
                .uri(url)
                .bodyValue(requestPayload)
                .retrieve()
                .bodyToMono(GeminiApiResponse.class)
                .map(response -> {
                    if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                        GeminiApiResponse.Candidate candidate = response.getCandidates().get(0);
                        if (candidate.getContent() != null && candidate.getContent().getParts() != null && !candidate.getContent().getParts().isEmpty()) {
                            return candidate.getContent().getParts().get(0).getText();
                        }
                    }
                    return "AI 응답을 생성하지 못했습니다.";
                })
                .onErrorResume(e -> {
                    log.error("Gemini API 호출 중 오류 발생: {}", e.getMessage(), e);
                    return Mono.just("AI 서비스와의 통신 중 오류가 발생했습니다: " + e.getMessage());
                });
    }

    @Override
    public Mono<List<GeminiModelResDTO>> getModelList() {
        String url = restApiProperties.getGemini().getBaseUrl() + "?key=" + apiKey;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GoogleModelListResponse.class)
                .map(response -> {
                    List<GeminiModelResDTO> list = new ArrayList<>();
                    if (response != null && response.getModels() != null) {
                        for (GoogleModelListResponse.GoogleModel model : response.getModels()) {
                            // 콘텐츠 생성이 가능한 텍스트 모델만 필터링
                            if (model.getSupportedGenerationMethods() != null &&
                                    model.getSupportedGenerationMethods().contains("generateContent")) {
                                
                                String id = model.getName();
                                if (id.startsWith("models/")) {
                                    id = id.substring("models/".length());
                                }

                                String koreanDesc = getFriendlyDescription(id, model.getDescription());

                                list.add(GeminiModelResDTO.builder()
                                        .id(id)
                                        .name(model.getDisplayName())
                                        .desc(koreanDesc)
                                        .build());
                            }
                        }
                    }
                    return list;
                })
                .onErrorResume(e -> {
                    log.error("Gemini 모델 목록 조회 중 오류 발생, 폴백 목록 사용: {}", e.getMessage(), e);
                    List<GeminiModelResDTO> fallback = List.of(
                            new GeminiModelResDTO("gemini-3.5-flash", "Gemini 3.5 Flash", "초고속 차세대 대형 언어 모델 (추천)"),
                            new GeminiModelResDTO("gemini-3.1-pro-preview", "Gemini 3.1 Pro (Preview)", "제미나이 3.1 고성능 분석 모델"),
                            new GeminiModelResDTO("gemini-2.5-flash", "Gemini 2.5 Flash", "안정적인 고속 분석 모델")
                    );
                    return Mono.just(fallback);
                });
    }

    private String getFriendlyDescription(String id, String defaultDesc) {
        if (id == null) return defaultDesc;
        
        switch (id) {
            case "gemini-3.5-flash":
                return "초고속 차세대 대형 언어 모델 (추천)";
            case "gemini-3.1-pro-preview":
                return "제미나이 3.1 고성능 분석 모델";
            case "gemini-3-flash-preview":
                return "제미나이 3.0 실험적 고속 모델";
            case "gemini-2.5-flash":
                return "안정적인 고속 분석 모델";
            case "gemini-2.5-pro":
                return "고도의 분석 및 복잡한 추론";
            case "gemini-2.0-flash":
                return "구형 고속 분석 모델";
            default:
                return defaultDesc != null ? defaultDesc : "";
        }
    }
}
