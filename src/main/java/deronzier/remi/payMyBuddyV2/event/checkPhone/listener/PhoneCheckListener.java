package deronzier.remi.paymybuddyv2.event.checkphone.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.Message;

import deronzier.remi.paymybuddyv2.event.checkphone.OnPhoneCheckCompleteEvent;

@Component
public class PhoneCheckListener implements ApplicationListener<OnPhoneCheckCompleteEvent> {

	@Value("${twilio.sender}")
	private String sender;

	@Value("${spring.application.name}")
	private String appName;

	@Override
	public void onApplicationEvent(OnPhoneCheckCompleteEvent event) {
		final String content = constructContent(event.getCode());
		Message.creator(
				new com.twilio.type.PhoneNumber(event.getReceiver().getPhoneNumber()),
				new com.twilio.type.PhoneNumber(sender),
				content)
				.create();
	}

	private String constructContent(final String code) {
		return appName + ": Your verification code is: " + code;
	}

}
