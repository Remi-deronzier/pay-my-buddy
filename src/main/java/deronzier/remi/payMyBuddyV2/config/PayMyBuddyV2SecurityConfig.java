package deronzier.remi.payMyBuddyV2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
public class PayMyBuddyV2SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Try to give access to /resources folder but epic fail!!!!
	 *
	 */

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web
//				.ignoring()
//				.antMatchers("/resources/**");
//	}

	@Override
	public void configure(HttpSecurity http) throws Exception {// @formatter:off
		http
				.authorizeRequests()
				.antMatchers("/signup",
						"/resources/**",
						"/js/**",
						"/css/**",
						"/login*",
						"/user/signup",
						"/registrationConfirm*",
						"/forgotPassword*",
						"/user/resetPassword*",
						"/user/changePassword*",
						"/user/savePassword*")
				.permitAll()
				.anyRequest().authenticated()

				.and()
				.formLogin().loginPage("/login").permitAll().loginProcessingUrl("/doLogin")
				.defaultSuccessUrl("/", true)

				.and()
				.logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/doLogout", "POST"))

				.and()
				.rememberMe()
				.rememberMeParameter("sticky")
				.rememberMeCookieName("sticky-cookie");
	} // @formatter:on

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
