package com.app.common.util;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClient webClient;

    //GET (비동기)
    public <T> Mono<T> getAsync(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }

    //GET (동기)
    public <T> T getSync(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType)
                .block(); // block()을 통해 결과를 기다림
    }

    //POST (비동기)
    public <T, V> Mono<T> postAsync(String url, V requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }

    //POST (동기)
    public <T, V> T postSync(String url, V requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
    
    //GET Flux (비동기 다건 조회)
    public <T> Flux<T> getFlux(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(responseType);
    }

    //GET Flux 동기 List로 변환
    public <T> List<T> getFluxAsList(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(responseType)
                .collectList()// Flux의 모든 데이터를 모아 Mono<List<T>>로 변환
                .block();
    }
}