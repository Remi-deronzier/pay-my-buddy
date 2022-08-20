package deronzier.remi.payMyBuddyV2.event.checkPhone;

import org.springframework.context.ApplicationEvent;

import deronzier.remi.payMyBuddyV2.model.User;

public class OnPhoneCheckCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private final String code;
	private final User receiver;

	public OnPhoneCheckCompleteEvent(final User receiver, final String code) {
		super(receiver);
		this.receiver = receiver;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public User getReceiver() {
		return receiver;
	}

}
