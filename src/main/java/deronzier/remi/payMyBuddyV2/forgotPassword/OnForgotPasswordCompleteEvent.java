package deronzier.remi.payMyBuddyV2.forgotPassword;

import org.springframework.context.ApplicationEvent;

import deronzier.remi.payMyBuddyV2.model.User;

public class OnForgotPasswordCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private final String appUrl;
	private final User user;

	public OnForgotPasswordCompleteEvent(final User user, final String appUrl) {
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