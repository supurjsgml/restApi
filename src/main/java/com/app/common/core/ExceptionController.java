package com.app.common.core;

import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.app.common.dto.ApiDocumentResponseDTO;
import com.app.common.dto.ApiDocumentResponseDTO.Error;
import com.app.common.dto.ApiDocumentResponseDTO.Error.HeaderError;;

@RestControllerAdvice
public class ExceptionController {
//  @ExceptionHandler({NoHandlerFoundException.class})
//  @ResponseStatus(HttpStatus.NOT_FOUND)
//  public ResultJson handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
//    return new ResultJson(false, e.getLocalizedMessage());
//  }
  
//  @ExceptionHandler({PermissionDeniedException.class})
//  @ResponseStatus(HttpStatus.FORBIDDEN)
//  public ResultJson handlePermissionDenied(PermissionDeniedException e, WebRequest request) {
//    return new ResultJson(false, e.getLocalizedMessage());
//  }
  
//  @ExceptionHandler({AccessDeniedException.class})
//  @ResponseStatus(HttpStatus.UNAUTHORIZED)
//  public ResultJson handleUnauthorized(AccessDeniedException e, WebRequest request) {
//    return new ResultJson(false, e.getLocalizedMessage());
//  }
  
//  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
//  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//  public ResultJson handleMethodNotAllowed(HttpRequestMethodNotSupportedException e, WebRequest request) {
//    return new ResultJson(false, e.getLocalizedMessage());
//  }
  
  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.OK)
  public Error handleMethodNotInvaild(MethodArgumentNotValidException e, WebRequest request) {
      String error = null;
      
      if (ObjectUtils.isNotEmpty(e.getBindingResult()) && !e.getBindingResult().getFieldErrors().isEmpty()) {
          error = e.getBindingResult().getFieldErrors()
              .stream()
              .map(el -> el.getField().concat(" : ").concat(el.getDefaultMessage()))
              .limit(1)
              .collect(Collectors.joining());
      }
      
      Error<Object> er = new Error<>(String.valueOf(HttpStatus.OK.value()));
      er.setHeader(HeaderError.builder().message(error).build());
      
      return er;
  }
  
//  @ExceptionHandler({Exception.class})
//  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  public ResultJson handleException(Exception e, WebRequest request) {
//    e.printStackTrace();
//    return new ResultJson(false, e.getLocalizedMessage());
//  }
}