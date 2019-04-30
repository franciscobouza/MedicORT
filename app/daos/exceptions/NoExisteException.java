package daos.exceptions;

public class NoExisteException extends Exception{
	public NoExisteException() {
	}

	public NoExisteException(String message) {
		super(message);
	}

	public NoExisteException(Throwable cause) {
		super(cause);
	}

	public NoExisteException(String message, Throwable cause) {
		super(message, cause);
	}
}
