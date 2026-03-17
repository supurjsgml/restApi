package com.app.common.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VirtualThreadUtil {

    //가상 스레드 전용 실행자 (주의 자바 21+ 아니면 불지르고 도망가라)
	private final ExecutorService virtualThreadExecutor;

    /**
     * N개의 작업을 가상 스레드에서 동시에 실행하고 결과를 리스트로 반환(동기적 대기) 
     * @param <T>
     * @param tasks
     * @author guney
     * @date 2026. 3. 17.
     */
    public <T> List<T> runAsyncMany(List<Supplier<T>> tasks) {
        List<CompletableFuture<T>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(task, virtualThreadExecutor))
                .toList();

        //모든 가상 스레드 작업이 완료될 때까지 기다림
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}