package deronzier.remi.paymybuddyv2.exception;

public class ExternalAccountNotFoundException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public ExternalAccountNotFoundException() {
		super();
	}

	public ExternalAccountNotFoundException(String msg) {
		super(msg);
	}

	public ExternalAccountNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
