package com.athmarine.exception;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"cause","stackTrace","suppressed","localizedMessage"})
public class PasswordMismatchException extends AppException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordMismatchException( String errorType, String errorCode, String message) {
		super(errorType, errorCode, message);
	}

	@Override
	public String toString() {
		return "PasswordMismatchException [getErrorType()=" + getErrorType() + ", getErrorCode()=" + getErrorCode()
				+ ", getMessage()=" + getMessage() + "]";
	}
	
}
