package deronzier.remi.paymybuddyv2.exception;

public class UserUnderEighteenException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public UserUnderEighteenException() {
		super();
	}

	public UserUnderEighteenException(String msg) {
		super(msg);
	}

	public UserUnderEighteenException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
