package deronzier.remi.paymybuddyv2.event.forgotpassword.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import deronzier.remi.paymybuddyv2.event.forgotpassword.OnForgotPasswordCompleteEvent;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.service.AuthenticationService;

@Component
public class ForgotPasswordListener implements ApplicationListener<OnForgotPasswordCompleteEvent> {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public void onApplicationEvent(final OnForgotPasswordCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(final OnForgotPasswordCompleteEvent event) {
		final User user = event.getUser();
		final String token = UUID.randomUUID().toString();
		authenticationService.createPasswordResetTokenForUser(user, token);
		final SimpleMailMessage email = constructEmailMessage(event, user, token);
		mailSender.send(email);
	}

	private SimpleMailMessage constructEmailMessage(final OnForgotPasswordCompleteEvent event, final User user,
			final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "Pay My Buddy - Reset password";
		final String changePasswordUrl = event.getAppUrl() + "/user/changePassword?userId=" + user.getId() + "&token="
				+ token;
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("Please open the following URL to verify your account: \r\n" + changePasswordUrl);
		return email;
	}

}
