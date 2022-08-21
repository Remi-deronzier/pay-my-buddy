package deronzier.remi.paymybuddyv2.exception;

public class AccountNotEnoughMoneyException extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public AccountNotEnoughMoneyException() {
		super();
	}

	public AccountNotEnoughMoneyException(String msg) {
		super(msg);
	}

	public AccountNotEnoughMoneyException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
