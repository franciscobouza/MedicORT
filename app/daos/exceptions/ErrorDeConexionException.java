package daos.exceptions;

public class ErrorDeConexionException extends Exception{
	public ErrorDeConexionException() {
	}

	public ErrorDeConexionException(String message) {
		super(message);
	}

	public ErrorDeConexionException(Throwable cause) {
		super(cause);
	}

	public ErrorDeConexionException(String message, Throwable cause) {
		super(message, cause);
	}
}
