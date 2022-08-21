package deronzier.remi.paymybuddyv2.exception;

public class NegativeAmountException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public NegativeAmountException() {
		super();
	}

	public NegativeAmountException(String msg) {
		super(msg);
	}

	public NegativeAmountException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
