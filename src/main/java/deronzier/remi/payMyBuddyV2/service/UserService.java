package deronzier.remi.paymybuddyv2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.paymybuddyv2.exception.ConnectionCreationException;
import deronzier.remi.paymybuddyv2.exception.ConnectionNotFoundException;
import deronzier.remi.paymybuddyv2.exception.UserEmailExistsException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.exception.UserUserNameExistsException;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.validation.passwordvalid.ValidPassword;

public interface UserService {

	Optional<User> findUserById(final int id);

	User addConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException;

	User deleteConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException;

	User save(User user);

	void delete(final int id) throws UserNotFoundException;

	List<User> findFuturePotentialConnections(final int ownerId) throws UserNotFoundException;

	User updateProfile(User inputUser, int id) throws UserNotFoundException;

	Page<User> findAll(Pageable pageable);

	User create(User newUser) throws UserEmailExistsException,
			UserUserNameExistsException;

	void changeUserPassword(User user, @ValidPassword String password);

	User findUserByEmail(String email) throws UserNotFoundException;

	Optional<User> findUserByUsername(final String userName);

}
