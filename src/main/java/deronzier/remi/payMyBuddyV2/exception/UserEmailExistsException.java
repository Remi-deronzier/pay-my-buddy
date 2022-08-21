package deronzier.remi.payMyBuddyV2.exception;

public class UserEmailExistsException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public UserEmailExistsException() {
		super();
	}

	public UserEmailExistsException(String msg) {
		super(msg);
	}

	public UserEmailExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
