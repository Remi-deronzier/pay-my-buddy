package deronzier.remi.payMyBuddyV2.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.twilio.Twilio;

import deronzier.remi.payMyBuddyV2.security.CustomAuthenticationProvider;
import deronzier.remi.payMyBuddyV2.security.CustomWebAuthenticationDetailsSource;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class PayMyBuddyV2SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${twilio.sid}")
	private String accountSid;

	@Value("${twilio.token}")
	private String authToken;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

	@Override
	public void configure(HttpSecurity http) throws Exception {// @formatter:off
		http
				.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/signup",
						"/resources/**",
						"/js/**",
						"/css/**",
						"/login*",
						"/user/signup",
						"/phoneCheck*",
						"/confirmPhoneNumber*",
						"/updatePhoneNumber*",
						"/resetRegistration*",
						"/user/signup",
						"/registrationConfirm*",
						"/qrCode*",
						"/confirmSecret*",
						"/forgotPassword*",
						"/user/resetPassword*",
						"/user/changePassword*",
						"/user/savePassword*")
				.permitAll()
				.anyRequest().authenticated()

				.and()
				.formLogin().loginPage("/login").permitAll().loginProcessingUrl("/doLogin")
				.defaultSuccessUrl("/", true)
				.authenticationDetailsSource(authenticationDetailsSource)

				.and()
				.logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/doLogout", "POST"))
				.deleteCookies("JSESSIONID")

				.and()
				.rememberMe()
				.userDetailsService(userDetailsService)
				.rememberMeParameter("sticky")
				.rememberMeCookieName("sticky-cookie");
	} // @formatter:on

	@Bean
	public DaoAuthenticationProvider authProvider() {
		final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@PostConstruct
	public void twilioRestClient() {
		Twilio.init(accountSid, authToken);
	}

}
