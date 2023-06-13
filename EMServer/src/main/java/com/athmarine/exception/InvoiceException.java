package com.athmarine.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "cause", "stackTrace", "suppressed", "localizedMessage" })
public class InvoiceException extends AppException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvoiceException(String errorType, String errorCode, String message) {
        super(errorType, errorCode, message);

    }
}
