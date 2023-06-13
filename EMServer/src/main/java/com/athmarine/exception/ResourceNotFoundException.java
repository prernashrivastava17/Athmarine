package com.athmarine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends AppException{
	
	private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String errorType, String errorCode, String message){
    	super(errorType, errorCode, message);
    }

}
