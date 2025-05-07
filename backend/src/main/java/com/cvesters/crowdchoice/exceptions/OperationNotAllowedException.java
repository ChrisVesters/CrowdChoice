package com.cvesters.crowdchoice.exceptions;

public class OperationNotAllowedException extends RuntimeException{
	
	public OperationNotAllowedException() {
		super();
	}

	public OperationNotAllowedException(final Throwable cause) {
		super(cause);
	}
}
