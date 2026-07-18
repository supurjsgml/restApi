package com.app.gemini.service;

import java.util.List;
import com.app.gemini.dto.req.GeminiReqDTO;
import com.app.gemini.dto.res.GeminiModelResDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GeminiService {
    Flux<String> getSummary(GeminiReqDTO reqDTO);
    Mono<List<GeminiModelResDTO>> getModelList();
}
