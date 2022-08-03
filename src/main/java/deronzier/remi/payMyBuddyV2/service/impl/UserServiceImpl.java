package deronzier.remi.payMyBuddyV2.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.ConnectionNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<User> findById(final int id) {
		return userRepository.findById(id);
	}

	public User addConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException {
		// Check that ownerId and newConnectionId are different
		if (ownerId == newConnectionId) {
			throw new ConnectionCreationException("You can't connect with yourself");
		}
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new UserNotFoundException("User not found. This Account does not exist."));
		User newConnection = userRepository.findById(newConnectionId)
				.orElseThrow(() -> new UserNotFoundException("User not found. Your connection does not exist."));
		owner.addConnection(newConnection);
		return userRepository.save(owner);
	}

	public User deleteConnection(final int ownerId, final int newConnectionId)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException {
		// Check that ownerId and newConnectionId are different
		if (ownerId == newConnectionId) {
			throw new ConnectionCreationException("You can't connect with yourself");
		}
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new UserNotFoundException("User not found. This Account does not exist."));
		User newConnection = userRepository.findById(newConnectionId)
				.orElseThrow(() -> new UserNotFoundException("User not found. Your connection does not exist."));

		// Check if connection exists before deleting it
		if (owner.getConnections().contains(newConnection)) {
			owner.removeConnection(newConnection);
			return userRepository.save(owner);
		} else {
			throw new ConnectionNotFoundException("Connection not found");
		}
	}

}
