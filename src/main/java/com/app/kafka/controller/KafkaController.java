package com.app.kafka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.common.core.annotations.ApiDocumentResponse;
import com.app.common.dto.ApiBodyDTO;
import com.app.common.dto.ApiDocumentResponseDTO.Success.HeaderSuccess;
import com.app.common.enums.MessageEnum;
import com.app.common.util.ApiResUtil;
import com.app.kafka.service.KafkaProducerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
@Tag(name = "카프카", description = "스카프카")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @ApiDocumentResponse
    @Operation(summary = "카프카 전송", description = "토픽에 적재 할거지롱")
    @GetMapping("/send")
    public ApiBodyDTO.Response<Object> send(@RequestParam String msg) {
    	kafkaProducerService.sendMessage("restApi", msg);
        return ApiResUtil.success(msg, HeaderSuccess.builder().build(), MessageEnum.SUCCESS.getCode());
    }
}