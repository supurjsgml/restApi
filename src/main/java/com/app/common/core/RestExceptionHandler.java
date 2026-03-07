package com.app.common.core;


import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.app.common.dto.ApiDocumentResponseDTO.Error;
import com.app.common.dto.ApiDocumentResponseDTO.Error.HeaderError;
import com.app.common.dto.res.ApiDataResponse;
import com.app.common.exception.ApiDataResponseSuccessException;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
//  @ExceptionHandler({NoHandlerFoundException.class})
//  @ResponseStatus(HttpStatus.NOT_FOUND)
//  public Error<Object> handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
//    return new Error<Object>();
//  }
  
//  @ExceptionHandler({PermissionDeniedException.class})
//  @ResponseStatus(HttpStatus.FORBIDDEN)
//  public Error<Object> handlePermissionDenied(PermissionDeniedException e, WebRequest request) {
//    return new Error<Object>();
//  }
  
//  @ExceptionHandler({AccessDeniedException.class})
//  @ResponseStatus(HttpStatus.UNAUTHORIZED)
//  public Error<Object> handleUnauthorized(AccessDeniedException e, WebRequest request) {
//    return new Error<Object>();
//  }
  
//  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
//  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//  public Error<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e, WebRequest request) {
//    return new Error<Object>();
//  }
  
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
  
//  @ExceptionHandler({Exception.class})
//  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  public Error<Object> handleException(Exception e, WebRequest request) {
//    e.printStackTrace();
//    return new Error<Object>();
//  }
}