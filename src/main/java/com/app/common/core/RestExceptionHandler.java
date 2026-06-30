package com.app.common.core;


import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.app.common.dto.ApiDocumentResponseDTO.Error;
import com.app.common.dto.ApiDocumentResponseDTO.Error.HeaderError;
import com.app.common.dto.res.ApiDataResponse;
import com.app.common.exception.ApiDataResponseSuccessException;
import com.app.kakao.service.KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

  private final KakaoService kakaoService;

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public Error<Object> handleMethodNotInvaild(MethodArgumentNotValidException e, WebRequest request) {
      String error = null;
      
      if (ObjectUtils.isNotEmpty(e.getBindingResult()) && !e.getBindingResult().getFieldErrors().isEmpty()) {
          error = e.getBindingResult().getFieldErrors()
              .stream()
              .map(el -> el.getField().concat(" : ").concat(el.getDefaultMessage()))
              .limit(1)
              .collect(Collectors.joining());
      }
      
      Error<Object> er = new Error<>();
      er.setHeader(HeaderError.builder().message(error).build());
      
      return er;
  }
  
  @ExceptionHandler(ApiDataResponseSuccessException.class)
  public ApiDataResponse<Object> handleRollbackSuccessException(ApiDataResponseSuccessException e) {
      log.warn("RollbackSuccessException - Code : {}, Msg : {} Data : {}", e.getCode(), e.getMessage(), e.getData());

      return new ApiDataResponse<Object>().ok(e.getData(), e.getCode(), e.getMessage());
  }
  
  @ExceptionHandler(NoResourceFoundException.class)
  public Error<Object> handleNoResourceFoundException(NoResourceFoundException e) {
      log.warn("Resource Not Found (404) : {}", e.getMessage());
      Error<Object> er = new Error<>();
      er.setHeader(HeaderError.builder().message("존재하지 않는 리소스입니다.").build());
      return er;
  }
  
  @ExceptionHandler({Exception.class})
  public Error<Object> handleException(Exception e, WebRequest request) {
      log.error("Server Exception ERROR : {}", e.getMessage(), e);
      
      // 서버 에러 알림 전송
      try {
          String errorMsg = "[restApi 서버 에러] " + e.getClass().getSimpleName() + " : " + e.getMessage();
          kakaoService.sendKakao(errorMsg.length() > 180 ? errorMsg.substring(0, 180) : errorMsg);
      } catch (Exception ex) {
          log.error("에러 알림톡 전송 중 오류 발생: {}", ex.getMessage());
      }

      Error<Object> er = new Error<>();
      er.setHeader(HeaderError.builder().message("서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.").build());
      return er;
  }
}