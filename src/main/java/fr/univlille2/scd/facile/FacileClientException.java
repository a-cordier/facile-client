package fr.univlille2.scd.facile;

import java.io.IOException;

public class FacileClientException extends IOException {

	private static final long serialVersionUID = 1L;

	public FacileClientException() {
	}

	public FacileClientException(String message) {
		super(message);
	}

	public FacileClientException(Throwable cause) {
		super(cause);
	}

	public FacileClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
