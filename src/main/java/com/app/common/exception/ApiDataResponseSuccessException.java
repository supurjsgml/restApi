package com.app.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiDataResponseSuccessException extends RuntimeException {
	
    private final String code;
    private final String message;
    private final Object data;
    private final String OK = String.valueOf(HttpStatus.OK.value());
    private static final String SUCCESS = "SUCCESS";
    
    public ApiDataResponseSuccessException(String message) {
    	super(message);
    	this.code = OK;
    	this.message = message;
		this.data = null;
    }

    public ApiDataResponseSuccessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
		this.data = null;
    }
    
    public ApiDataResponseSuccessException(Object data) {
    	this.code = OK;
    	this.message = SUCCESS;
    	this.data = data;
    }
    
    public ApiDataResponseSuccessException(Object data, String message) {
    	super(message);
    	this.code = OK;
    	this.message = message;
    	this.data = data;
    }
    
    public ApiDataResponseSuccessException(Object data, String code, String message) {
    	super(message);
    	this.code = code;
    	this.message = message;
    	this.data = data;
    }
}
