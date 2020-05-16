package com.alphamart.exception;

import org.springframework.http.HttpStatus;

public class ErrorDetail {
	 
    private HttpStatus status;
    private String message;
    private String errors;
 
   public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}
    
    
}