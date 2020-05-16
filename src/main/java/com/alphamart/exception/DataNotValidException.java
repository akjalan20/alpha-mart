package com.alphamart.exception;

public class DataNotValidException extends Exception{

	private static final long serialVersionUID = 1L;

	public DataNotValidException(String msg) {
		super(msg);
	}

	public DataNotValidException(String msg, Throwable t) {
		super(msg, t);
	}
}
