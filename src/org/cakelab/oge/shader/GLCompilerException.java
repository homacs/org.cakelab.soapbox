package org.cakelab.oge.shader;

@SuppressWarnings("serial")
public class GLCompilerException extends GLException {

	public GLCompilerException() {
		super();
	}

	public GLCompilerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GLCompilerException(String message, Throwable cause) {
		super(message, cause);
	}

	public GLCompilerException(String message) {
		super(message);
	}

	public GLCompilerException(Throwable cause) {
		super(cause);
	}

}
