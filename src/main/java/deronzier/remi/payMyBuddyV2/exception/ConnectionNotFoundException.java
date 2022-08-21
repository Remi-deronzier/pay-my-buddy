package deronzier.remi.paymybuddyv2.exception;

public class ConnectionNotFoundException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public ConnectionNotFoundException() {
		super();
	}

	public ConnectionNotFoundException(String msg) {
		super(msg);
	}

	public ConnectionNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
