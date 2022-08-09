package deronzier.remi.payMyBuddyV2.exception;

public class ExternalAccountNotBelongGoodUserException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public ExternalAccountNotBelongGoodUserException() {
		super();
	}

	public ExternalAccountNotBelongGoodUserException(String msg) {
		super(msg);
	}

	public ExternalAccountNotBelongGoodUserException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
