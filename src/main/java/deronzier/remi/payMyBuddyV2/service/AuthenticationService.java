package deronzier.remi.payMyBuddyV2.service;

import java.io.UnsupportedEncodingException;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.PasswordResetToken;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.model.VerificationToken;

public interface AuthenticationService {

	/**
	 * https://github.com/google/google-authenticator/wiki/Key-Uri-Format To
	 * understand the format of the secret key to send to Google Authenticator
	 */
	String generateQRUrl(User user) throws UnsupportedEncodingException;

	void createPhoneVerificationCode(User user);

	PasswordResetToken getPasswordResetToken(String token);

	void createPasswordResetTokenForUser(User user, String token);

	VerificationToken getVerificationToken(String token);

	void createVerificationTokenForUser(User user, String token);

	User updateUsing2FA(boolean using2fa, int id) throws UserNotFoundException;

	User updatePhoneNumber(String phoneNumber, String userName) throws UserNotFoundException;

	boolean isPhoneNumberValid(String phoneNumber);

}
