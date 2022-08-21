package deronzier.remi.paymybuddyv2.exception;

public class ConnectionCreationException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public ConnectionCreationException() {
		super();
	}

	public ConnectionCreationException(String msg) {
		super(msg);
	}

	public ConnectionCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
