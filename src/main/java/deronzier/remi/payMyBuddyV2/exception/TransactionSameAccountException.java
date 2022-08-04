package deronzier.remi.payMyBuddyV2.exception;

public class TransactionSameAccountException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public TransactionSameAccountException() {
		super();
	}

	public TransactionSameAccountException(String msg) {
		super(msg);
	}

	public TransactionSameAccountException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
