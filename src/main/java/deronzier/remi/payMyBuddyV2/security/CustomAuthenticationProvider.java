package deronzier.remi.paymybuddyv2.security;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.repository.UserRepository;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails())
				.getVerificationCode();
		User user = userRepository.findByUserName(auth.getName())
				.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
		if (user.isUsing2FA()) {
			Totp totp = new Totp(user.getSecret());
			if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
				throw new BadCredentialsException("Invalid verfication code");
			}
		}

		Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(
				result.getPrincipal(), result.getCredentials(), result.getAuthorities());
	}

	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
