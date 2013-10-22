package com.ap.io;

import java.io.PrintStream;
import java.io.PrintWriter;

public class StoreException extends RuntimeException {

	public StoreException() {
		super();
	}

	public StoreException(String message) {
		super(message);
	}

	public StoreException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	public StoreException(Throwable cause) {
		this.cause = cause;
	}
	
    public String getMessage() {
        return cause != null ? cause.getMessage() : super.getMessage();
    }

	public void printStackTrace() {
		super.printStackTrace();
		if (cause != null) cause.printStackTrace();
	}

	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		if (cause != null) cause.printStackTrace(s);
	}

	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		if (cause != null) cause.printStackTrace(s);
	}

	private Throwable cause = this;

}
