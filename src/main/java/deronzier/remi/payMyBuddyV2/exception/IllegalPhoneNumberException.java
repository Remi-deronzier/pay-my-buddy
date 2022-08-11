package deronzier.remi.payMyBuddyV2.exception;

public class IllegalPhoneNumberException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public IllegalPhoneNumberException() {
		super();
	}

	public IllegalPhoneNumberException(String msg) {
		super(msg);
	}

	public IllegalPhoneNumberException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
