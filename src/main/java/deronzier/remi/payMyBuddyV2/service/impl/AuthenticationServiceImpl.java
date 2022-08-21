package deronzier.remi.paymybuddyv2.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twilio.rest.lookups.v1.PhoneNumber;

import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.PasswordResetToken;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.model.VerificationToken;
import deronzier.remi.paymybuddyv2.repository.PasswordResetTokenRepository;
import deronzier.remi.paymybuddyv2.repository.UserRepository;
import deronzier.remi.paymybuddyv2.repository.VerificationTokenRepository;
import deronzier.remi.paymybuddyv2.service.AuthenticationService;
import deronzier.remi.paymybuddyv2.utils.Constants;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Value("${spring.application.name}")
	private String appName;

	@Override
	public void createVerificationTokenForUser(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);
		verificationTokenRepository.save(myToken);
	}

	@Override
	public VerificationToken getVerificationToken(final String token) {
		return verificationTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPhoneVerificationCode(User user) {
		final String phoneVerificationCode = getRandomNumberString();
		user.setPasswordConfirmation(user.getPassword());
		user.setPhoneVerificationCode(phoneVerificationCode);
		userRepository.save(user);
	}

	@Override
	public User updateUsing2FA(boolean using2FA, int id) throws UserNotFoundException {
		User userToUpdate = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		userToUpdate.setPasswordConfirmation(userToUpdate.getPassword());
		userToUpdate.setUsing2FA(using2FA);
		userToUpdate.setSecret(Base32.random());
		return userRepository.save(userToUpdate);
	}

	@Override
	public User updatePhoneNumber(String phoneNumber, String userName)
			throws UserNotFoundException {
		User userToUpdate = userRepository.findByUserName(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		userToUpdate.setPasswordConfirmation(userToUpdate.getPassword());
		userToUpdate.setPhoneNumber(phoneNumber);
		return userRepository.save(userToUpdate);
	}

	@Override
	public boolean isPhoneNumberValid(String phoneNumber) {
		try {
			PhoneNumber
					.fetcher(new com.twilio.type.PhoneNumber(phoneNumber))
					.fetch();
			return true;
		} catch (com.twilio.exception.ApiException e) {
			return false;
		}
	}

	/**
	 * https://github.com/google/google-authenticator/wiki/Key-Uri-Format To
	 * understand the format of the secret key to send to Google Authenticator
	 */
	@Override
	public String generateQRUrl(User user) throws UnsupportedEncodingException {
		return Constants.QR_PREFIX + URLEncoder.encode(String.format(
				"otpauth://totp/%s:%s?secret=%s&issuer=%s",
				appName, user.getEmail(), user.getSecret(), appName),
				"UTF-8");
	}

	private String getRandomNumberString() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}

}
