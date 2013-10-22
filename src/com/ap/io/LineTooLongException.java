package com.ap.io;

public class LineTooLongException extends StoreException {

	public LineTooLongException() {
		super();
	}

	public LineTooLongException(String message) {
		super(message);
	}

	public LineTooLongException(String message, Throwable cause) {
		super(message, cause);
	}

	public LineTooLongException(Throwable cause) {
		super(cause);
	}
	
}
