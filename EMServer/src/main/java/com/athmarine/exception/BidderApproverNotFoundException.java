package com.athmarine.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "cause", "stackTrace", "suppressed", "localizedMessage" })
public class BidderApproverNotFoundException extends AppException{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BidderApproverNotFoundException(String errorType, String errorCode, String message) {
		super(errorType, errorCode, message);
		// TODO Auto-generated constructor stub
	}
	
	

}
