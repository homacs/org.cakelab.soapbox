package org.cakelab.oge.shader;

@SuppressWarnings("serial")
public class GLException extends Exception {

	public GLException() {
		super();
	}

	public GLException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GLException(String message, Throwable cause) {
		super(message, cause);
	}

	public GLException(String message) {
		super(message);
	}

	public GLException(Throwable cause) {
		super(cause);
	}

}
