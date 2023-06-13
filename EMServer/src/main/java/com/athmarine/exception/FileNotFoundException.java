package com.athmarine.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "cause", "stackTrace", "suppressed", "localizedMessage" })
public class FileNotFoundException extends AppException {

	public FileNotFoundException(String errorType, String errorCode, String message) {
		super(errorType, errorCode, message);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}