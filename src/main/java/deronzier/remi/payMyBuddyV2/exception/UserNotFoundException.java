package deronzier.remi.paymybuddyv2.exception;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 5093736681926400195L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String msg) {
		super(msg);
	}

	public UserNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
