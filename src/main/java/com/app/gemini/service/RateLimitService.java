package com.app.gemini.service;

import reactor.core.publisher.Mono;

public interface RateLimitService {
    Mono<Boolean> checkLimit(String email, String googleSubId);
}
