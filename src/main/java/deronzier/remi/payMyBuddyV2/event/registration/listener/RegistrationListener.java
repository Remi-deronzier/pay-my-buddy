package deronzier.remi.payMyBuddyV2.event.registration.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import deronzier.remi.payMyBuddyV2.event.registration.OnRegistrationCompleteEvent;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.AuthenticationService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(final OnRegistrationCompleteEvent event) {
		final User user = event.getUser();
		final String token = UUID.randomUUID().toString();
		authenticationService.createVerificationTokenForUser(user, token);

		final SimpleMailMessage email = constructEmailMessage(event, user, token);
		mailSender.send(email);
	}

	private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user,
			final String token) {
		final String recipientAddress = user.getEmail();
		final String subject = "Pay My Buddy - Registration confirmation";
		final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("Please open the following URL to verify your account: \r\n" + confirmationUrl);
		return email;
	}

}
