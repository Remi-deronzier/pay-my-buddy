package deronzier.remi.paymybuddyv2.exception;

public class UserUserNameExistsException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public UserUserNameExistsException() {
		super();
	}

	public UserUserNameExistsException(String msg) {
		super(msg);
	}

	public UserUserNameExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
