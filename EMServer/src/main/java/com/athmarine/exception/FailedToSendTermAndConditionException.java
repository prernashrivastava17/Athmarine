package com.athmarine.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "cause", "stackTrace", "suppressed", "localizedMessage" })
public class FailedToSendTermAndConditionException extends AppException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FailedToSendTermAndConditionException(String errorType, String errorCode, String message) {
		super(errorType, errorCode, message);

	}

}