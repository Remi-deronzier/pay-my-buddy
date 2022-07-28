package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Optional<User> getUser(final int id) {
		return userRepository.findById(id);
	}

	public User addConnection(final int ownerId, final int newConnectionId) throws UserNotFoundException {
		User owner = getUser(ownerId)
				.orElseThrow(() -> new UserNotFoundException("User not found. This Account does not exist."));
		User newConnection = getUser(newConnectionId)
				.orElseThrow(() -> new UserNotFoundException("User not found. Your connection does not exist."));
		owner.addConnection(newConnection);
		return userRepository.save(owner);
	}

}
