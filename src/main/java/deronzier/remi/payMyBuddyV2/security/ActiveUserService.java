package deronzier.remi.paymybuddyv2.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class ActiveUserService {

	@Autowired
	private SessionRegistry sessionRegistry;

	public final List<String> getActiveUsers() { // @formatter:off
		final List<Object> principals = sessionRegistry.getAllPrincipals();
		final User[] users = principals.toArray(new User[principals.size()]);

		return Arrays.stream(users)
				.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
				.map(u -> u.getUsername())
				.collect(Collectors.toList());
	} // @formatter:on

	public final boolean isUserActive(String userName) {
		final List<Object> principals = sessionRegistry.getAllPrincipals();
		final User[] users = principals.toArray(new User[principals.size()]);

		return Arrays.stream(users)
				.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
				.filter(u -> u.getUsername().equals(userName))
				.collect(Collectors.toList()).size() == 1;
	}

}
