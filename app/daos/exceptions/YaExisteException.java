package daos.exceptions;

public class YaExisteException extends Exception{
	public YaExisteException() {
	}

	public YaExisteException(String message) {
		super(message);
	}

	public YaExisteException(Throwable cause) {
		super(cause);
	}

	public YaExisteException(String message, Throwable cause) {
		super(message, cause);
	}
}
