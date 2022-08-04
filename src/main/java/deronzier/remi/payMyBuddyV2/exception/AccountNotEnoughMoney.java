package deronzier.remi.payMyBuddyV2.exception;

public class AccountNotEnoughMoney extends Exception {
	private static final long serialVersionUID = 5093736681926400195L;

	public AccountNotEnoughMoney() {
		super();
	}

	public AccountNotEnoughMoney(String msg) {
		super(msg);
	}

	public AccountNotEnoughMoney(String msg, Throwable cause) {
		super(msg, cause);
	}
}
