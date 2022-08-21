package deronzier.remi.paymybuddyv2.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.paymybuddyv2.model.Role;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.repository.UserRepository;

@Service
@Transactional
public class PayMyBuddyV2UserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new CustomUser(user.getUserName(), user.getPassword(),
				user.isEnabled(),
				true,
				true, true, getAuthorities(user.getRoles()), user.getId());
	}

	private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

}
