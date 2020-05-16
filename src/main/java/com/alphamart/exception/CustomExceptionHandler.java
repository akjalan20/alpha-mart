package com.alphamart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorDetail> handle(NotFoundException e) {
		ErrorDetail error = new ErrorDetail();
        error.setMessage(e.getMessage());
        error.setErrors(e.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<ErrorDetail>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ErrorDetail> handle(InvalidRequestException e) {
		ErrorDetail error = new ErrorDetail();
        error.setMessage(e.getMessage());
        error.setErrors(e.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ErrorDetail>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(DataNotValidException.class)
	public ResponseEntity<ErrorDetail> handle(DataNotValidException e) {
		ErrorDetail error = new ErrorDetail();
        error.setMessage(e.getMessage());
        error.setErrors(e.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ErrorDetail>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorDetail> handle(RuntimeException e) {
		ErrorDetail error = new ErrorDetail();
        error.setMessage(e.getMessage());
        error.setErrors(e.toString());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<ErrorDetail>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
}