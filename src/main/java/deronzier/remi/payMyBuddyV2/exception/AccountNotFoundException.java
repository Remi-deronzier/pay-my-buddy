package deronzier.remi.payMyBuddyV2.exception;

public class AccountNotFoundException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public AccountNotFoundException() {
		super();
	}

	public AccountNotFoundException(String msg) {
		super(msg);
	}

	public AccountNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
