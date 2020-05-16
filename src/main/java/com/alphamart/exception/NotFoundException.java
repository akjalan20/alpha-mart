package com.alphamart.exception;

public class NotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
