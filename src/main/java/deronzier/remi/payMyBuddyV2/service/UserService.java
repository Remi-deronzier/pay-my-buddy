package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;

public interface UserService {

	Optional<User> findById(final int id);

	User addConnection(final int ownerId, final int newConnectionId) throws UserNotFoundException;

}
