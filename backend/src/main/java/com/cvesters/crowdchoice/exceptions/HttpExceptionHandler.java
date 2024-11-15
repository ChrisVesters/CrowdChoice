package com.cvesters.crowdchoice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HttpExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Void> handle(final NotFoundException e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Void> handle(final ConflictException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleUnexpected(final Exception e) {
		return ResponseEntity.internalServerError().build();
	}
}