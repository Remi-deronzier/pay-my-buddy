package deronzier.remi.payMyBuddyV2.event.bankFlow.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.Message;

import deronzier.remi.payMyBuddyV2.event.bankFlow.OnBankFlowCompleteEvent;

@Component
public class BankFlowListener implements ApplicationListener<OnBankFlowCompleteEvent> {

	@Value("${twilio.sender}")
	private String sender;

	@Value("${spring.application.name}")
	private String appName;

	@Override
	public void onApplicationEvent(OnBankFlowCompleteEvent event) {
		final String content = constructContent(event.getAmount());
		Message.creator(
				new com.twilio.type.PhoneNumber(event.getReceiver().getPhoneNumber()),
				new com.twilio.type.PhoneNumber(sender),
				content)
				.create();
	}

	private String constructContent(final double amount) {
		return appName + ": " + "A transfer of " + amount
				+ " EUR has been initiated on your account. If you did not initiate the transfer, please contact the help center";
	}

}
