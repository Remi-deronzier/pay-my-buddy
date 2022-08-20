package deronzier.remi.payMyBuddyV2.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.ConnectionNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.IllegalPhoneNumberException;
import deronzier.remi.payMyBuddyV2.exception.UserEmailExistsException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserUserNameExistsException;
import deronzier.remi.payMyBuddyV2.model.PasswordResetToken;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.model.VerificationToken;

public interface UserService {

	Optional<User> findUserById(final int id);

	User addConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException;

	User deleteConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException;

	User save(User user);

	void delete(final int id) throws UserNotFoundException;

	List<User> findFuturePotentialConnections(final int ownerId) throws UserNotFoundException;

	User updateProfile(User inputUser, int id) throws UserNotFoundException, IllegalPhoneNumberException;

	Page<User> findAll(Pageable pageable);

	User create(User newUser) throws UserEmailExistsException,
			UserUserNameExistsException;

	VerificationToken getVerificationToken(String token);

	void createVerificationTokenForUser(User user, String token);

	void createPasswordResetTokenForUser(User user, String token);

	PasswordResetToken getPasswordResetToken(String token);

	void changeUserPassword(User user, String password);

	User findUserByEmail(String email) throws UserNotFoundException;

	Optional<User> findUserByUsername(final String userName);

	String generateQRUrl(User user) throws UnsupportedEncodingException;

	User updateUsing2FA(boolean using2fa, int id) throws UserNotFoundException;

	void createPhoneVerificationCode(User user);

	User updatePhoneNumber(String phoneNumber, String userName)
			throws UserNotFoundException, IllegalPhoneNumberException;

}
