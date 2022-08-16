package deronzier.remi.payMyBuddyV2.registration;

import org.springframework.context.ApplicationEvent;

import deronzier.remi.payMyBuddyV2.model.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private final String appUrl;
	private final User user;

	public OnRegistrationCompleteEvent(final User user, final String appUrl) {
		super(user);
		this.user = user;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public User getUser() {
		return user;
	}

}