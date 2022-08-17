package deronzier.remi.payMyBuddyV2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.ConnectionNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.IllegalPhoneNumberException;
import deronzier.remi.payMyBuddyV2.exception.UserEmailExistsException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserUserNameExistsException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.PasswordResetToken;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.model.VerificationToken;
import deronzier.remi.payMyBuddyV2.repository.PasswordResetTokenRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.repository.VerificationTokenRepository;
import deronzier.remi.payMyBuddyV2.service.UserService;
import deronzier.remi.payMyBuddyV2.utils.Constants;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	public Optional<User> findById(final int id) {
		return userRepository.findById(id);
	}

	@Override
	public User addConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException {
		// Check that ownerId and newConnectionId are different
		if (ownerId == newConnectionId) {
			throw new ConnectionCreationException("You can't connect with yourself");
		}
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		User newConnection = userRepository.findById(newConnectionId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		owner.addConnection(newConnection);
		return userRepository.save(owner);
	}

	@Override
	public User deleteConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException {
		// Check that ownerId and newConnectionId are different
		if (ownerId == newConnectionId) {
			throw new ConnectionCreationException("You can't connect with yourself");
		}
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		User newConnection = userRepository.findById(newConnectionId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		// Check if connection exists before deleting it
		if (owner.getConnections().contains(newConnection)) {
			owner.removeConnection(newConnection);
			return userRepository.save(owner);
		} else {
			throw new ConnectionNotFoundException("Connection not found");
		}
	}

	@Override
	public User create(User newUser)
			throws UserEmailExistsException,
			UserUserNameExistsException {
		// Create new account and set balance of account to 0
		Account newAccount = new Account();
		newAccount.setBalance(Constants.INITIAL_ACCOUNT_BALANCE);

		// Synchronize relation between account and user
		newUser.addAcount(newAccount);

		// Check uniqueness of email and user name
		if (emailExists(newUser.getEmail())) {
			throw new UserEmailExistsException("There is an account with that email address " + newUser.getEmail());
		}
		if (userNameExists(newUser.getUserName())) {
			throw new UserUserNameExistsException("There is an account with that user name " + newUser.getUserName());
		}

		String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
		newUser.setPassword(encryptedPassword);
		newUser.setPasswordConfirmation(encryptedPassword);

		// Save new user
		return userRepository.save(newUser);
	}

	@Override
	public void delete(int id) throws UserNotFoundException {
		userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		userRepository.deleteById(id);
	}

	@Override
	public User save(User user) {
		user.setPasswordConfirmation(user.getPassword());
		return userRepository.save(user);
	}

	private boolean emailExists(String email) {
		final Optional<User> user = userRepository.findByEmail(email);
		return user.isPresent();
	}

	private boolean userNameExists(String userName) {
		final Optional<User> user = userRepository.findByUserName(userName);
		return user.isPresent();
	}

	@Override
	public User updateProfile(User inputUser, int id) throws UserNotFoundException, IllegalPhoneNumberException {
		User userToUpdate = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		userToUpdate.setPasswordConfirmation(userToUpdate.getPassword());
		userToUpdate.setUserName(inputUser.getUserName());
		userToUpdate.setEmail(inputUser.getEmail());
		userToUpdate.setFirstName(inputUser.getFirstName());
		userToUpdate.setLastName(inputUser.getLastName());
		userToUpdate.setPhoneNumber(inputUser.getPhoneNumber());
		userToUpdate.setDescription(inputUser.getDescription());
		return userRepository.save(userToUpdate);
	}

	@Override
	public List<User> findFuturePotentialConnections(int ownerId) throws UserNotFoundException {
		// Get all users
		Iterable<User> allUsers = userRepository.findAll();

		// Convert users iterable to list and delete user logged in
		List<User> futurePotentialConnections = new ArrayList<User>();
		allUsers.forEach((user) -> {
			if (user.getId() != ownerId) {
				futurePotentialConnections.add(user);
			}
		});

		// Find owner
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new UserNotFoundException("User not found."));

		// Get owner's connections
		List<User> connections = owner.getConnections();

		// Keep only users that are not in his connections
		futurePotentialConnections.removeAll(connections);

		return futurePotentialConnections;
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public User findUserByEmail(final String email) throws UserNotFoundException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found."));

	}

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
	public void changeUserPassword(final User user, final String password) {
		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		user.setPasswordConfirmation(encryptedPassword);
		userRepository.save(user);
	}
}
