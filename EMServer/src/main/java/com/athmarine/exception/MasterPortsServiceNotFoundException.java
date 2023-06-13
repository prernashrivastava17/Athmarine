package com.athmarine.exception;

public class MasterPortsServiceNotFoundException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MasterPortsServiceNotFoundException(String errorType, String errorCode, String message) {
		super(errorType, errorCode, message);

	}

}
