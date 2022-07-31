package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;

public interface UserService {

	Optional<User> findById(final int id);

	Page<User> findAllConnections(int ownerId, Pageable pageable);

	User addConnection(final int ownerId, final int newConnectionId) throws UserNotFoundException;

}
