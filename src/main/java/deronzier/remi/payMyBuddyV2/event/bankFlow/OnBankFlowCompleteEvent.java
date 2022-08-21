package deronzier.remi.paymybuddyv2.event.bankflow;

import org.springframework.context.ApplicationEvent;

import deronzier.remi.paymybuddyv2.model.User;

public class OnBankFlowCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private final double amount;
	private final User receiver;

	public OnBankFlowCompleteEvent(final User receiver, final double amount) {
		super(receiver);
		this.receiver = receiver;
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public User getReceiver() {
		return receiver;
	}

}
