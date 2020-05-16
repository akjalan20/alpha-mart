package com.alphamart.exception;

public class InvalidRequestException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String msg) {
		super(msg);
	}

	public InvalidRequestException(String msg, Throwable t) {
		super(msg, t);
	}
}
