package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.ConnectionNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;

public interface UserService {

	Optional<User> findById(final int id);

	User addConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException;

	User deleteConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException;

	User create();

	User save(User user);

	void delete(final int id) throws UserNotFoundException;

	static final double INITIAL_ACCOUNT_BALANCE = 0;
}
