package deronzier.remi.payMyBuddyV2.controller;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import deronzier.remi.payMyBuddyV2.event.checkPhone.OnPhoneCheckCompleteEvent;
import deronzier.remi.payMyBuddyV2.event.forgotPassword.OnForgotPasswordCompleteEvent;
import deronzier.remi.payMyBuddyV2.event.registration.OnRegistrationCompleteEvent;
import deronzier.remi.payMyBuddyV2.exception.UserEmailExistsException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserUserNameExistsException;
import deronzier.remi.payMyBuddyV2.model.PasswordResetToken;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.model.VerificationToken;
import deronzier.remi.payMyBuddyV2.service.AuthenticationService;
import deronzier.remi.payMyBuddyV2.service.UserService;

@Controller
public class AuthenticationController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Log in

	@RequestMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			String errorMessage = (String) inputFlashMap.get("errorMessage");
			String message = (String) inputFlashMap.get("message");
			model.addAttribute("message", message);
			model.addAttribute("errorMessage", errorMessage);
		}
		return "authentication/login-view";
	}

	// Sign up

	@RequestMapping("/signup")
	public String getSignup(Model model) {
		User newUser = new User();
		model.addAttribute("newUser", newUser);
		return "authentication/signup-view";
	}

	@RequestMapping("/user/signup")
	public String postSignup(@Valid @ModelAttribute("newUser") final User newUser, final BindingResult result,
			Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			return returnSignupPageWithErrorMessages(model, newUser);
		}

		if (!authenticationService.isPhoneNumberValid(newUser.getPhoneNumber())) {
			result.addError(new FieldError("newUser", "phoneNumber", "Invalid phone number"));
			return returnSignupPageWithErrorMessages(model, newUser);
		}

		try {
			final User registeredUser = userService.create(newUser);

			final String appUrl = createAppUrl(request);
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, appUrl));
		} catch (UserEmailExistsException ueee) {
			result.addError(new FieldError("newUser", "email", ueee.getMessage()));
			return returnSignupPageWithErrorMessages(model, newUser);
		} catch (UserUserNameExistsException uunee) {
			result.addError(new FieldError("newUser", "userName", uunee.getMessage()));
			return returnSignupPageWithErrorMessages(model, newUser);
		}

		return "redirect:/login?registrationSuccessfull";
	}

	private String returnSignupPageWithErrorMessages(Model model, User newUser) {
		model.addAttribute("newUser", newUser);
		model.addAttribute("hasCustomErrors", true);
		return "authentication/signup-view";
	}

	@RequestMapping(value = "/registrationConfirm")
	public ModelAndView confirmRegistration(final Model model, @RequestParam("token") final String token,
			final RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		final VerificationToken verificationToken = authenticationService.getVerificationToken(token);
		if (verificationToken == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
			return new ModelAndView("redirect:/login");
		}

		final User user = verificationToken.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Your registration token has expired. Please register again.");
			return new ModelAndView("redirect:/login");
		}

		redirectAttributes.addAttribute("userName", user.getUserName());
		return new ModelAndView("redirect:/phoneCheck");

	}

	// Reset password

	@RequestMapping("/forgotPassword")
	public String showForgotPassword() {
		return "authentication/forgot-password";
	}

	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail,
			final RedirectAttributes redirectAttributes) {
		User user;
		try {
			user = userService.findUserByEmail(userEmail);
			final String appUrl = createAppUrl(request);
			eventPublisher.publishEvent(new OnForgotPasswordCompleteEvent(user, appUrl));
			redirectAttributes.addFlashAttribute("message", "You should receive a password reset email shortly");
			return new ModelAndView("redirect:/login");
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"It seems that this user does not exist. You must sign up first");
			return new ModelAndView("redirect:/signup");
		}
	}

	private String createAppUrl(final HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
	}

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public ModelAndView showChangePasswordPage(@RequestParam("userId") final long userId,
			@RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
		final PasswordResetToken passToken = authenticationService.getPasswordResetToken(token);
		if (passToken == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
			return new ModelAndView("redirect:/login");
		}
		final User user = passToken.getUser();
		if (user.getId() != userId) {
			redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
			return new ModelAndView("redirect:/login");
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "Your password reset token has expired");
			return new ModelAndView("redirect:/login");
		}

		final ModelAndView view = new ModelAndView("authentication/reset-password");
		view.addObject("token", token);
		view.addObject("userId", userId);
		view.addObject("userName", user.getUserName());
		return view;
	}

	@RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView savePassword(@RequestParam("password") final String password,
			@RequestParam("passwordConfirmation") final String passwordConfirmation,
			@RequestParam("token") final String token, @RequestParam("userId") final long userId,
			final RedirectAttributes redirectAttributes) {
		try {
			if (!password.equals(passwordConfirmation)) {
				return redirectToChangePasswordWithErrorMessage(token, userId, redirectAttributes,
						"Passwords do not match");
			}

			final PasswordResetToken passwordResetToken = authenticationService.getPasswordResetToken(token);
			if (passwordResetToken == null) {
				redirectAttributes.addFlashAttribute("errorMessage", "Invalid token");
			} else {
				final User user = passwordResetToken.getUser();
				if (user == null) {
					redirectAttributes.addFlashAttribute("errorMessage", "Unknown user");
				} else {
					final String encryptedPreviousPassword = user.getPassword();
					if (passwordEncoder.matches(password, encryptedPreviousPassword)) {
						return redirectToChangePasswordWithErrorMessage(token, userId, redirectAttributes,
								"This new password must be different from the old one");
					}
					userService.changeUserPassword(user, password);
					redirectAttributes.addFlashAttribute("message", "Password reset successfully");
				}
			}
			return new ModelAndView("redirect:/login");
		} catch (ConstraintViolationException cve) {
			redirectAttributes.addFlashAttribute("passwordErrorMessage", cve.getMessage().substring(29)); // Delete the
																											// unnecessary
																											// beginning
																											// of the
																											// message
			return redirectToChangePasswordWithErrorMessage(token, userId, redirectAttributes, "Invalid password");
		}
	}

	private ModelAndView redirectToChangePasswordWithErrorMessage(final String token, final long userId,
			final RedirectAttributes redirectAttributes, final String errorMessage) {
		redirectAttributes.addAttribute("token", token);
		redirectAttributes.addAttribute("userId", userId);
		redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		return new ModelAndView("redirect:/user/changePassword");
	}

	// 2FA

	@RequestMapping("/qrCode")
	public String getQrCode(Model model, HttpServletRequest request) {
		String qrCode = (String) request.getParameter("qrCode");
		String userName = (String) request.getParameter("username");
		model.addAttribute("qrCode", qrCode);
		model.addAttribute("userName", userName);
		return "authentication/qr-code";
	}

	@PostMapping("/confirmSecret")
	public String signupConfirmSecret2FA(@RequestParam("userName") String userName,
			@RequestParam(required = true, value = "code") String code, RedirectAttributes redirectAttributes)
			throws UserNotFoundException, UnsupportedEncodingException {

		User user = userService.findUserByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		Totp totp = new Totp(user.getSecret());
		if (totp.verify(code)) {
			redirectAttributes.addFlashAttribute("message", "Successful implementation of 2FA");
			return "redirect:/login";
		} else {
			redirectAttributes.addAttribute("qrCode", authenticationService.generateQRUrl(user));
			redirectAttributes.addAttribute("userName", user.getUserName());
			return "redirect:/qrCode?errorMessage";
		}
	}

	// Phone verification

	@RequestMapping("/phoneCheck")
	public String getCheckPhoneNumber(Model model, @RequestParam("userName") String userName,
			RedirectAttributes redirectAttributes)
			throws UserNotFoundException, UnsupportedEncodingException {
		User user = userService.findUserByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		try {
			authenticationService.createPhoneVerificationCode(user);
			model.addAttribute("phoneNumber", user.getPhoneNumber());
			eventPublisher.publishEvent(new OnPhoneCheckCompleteEvent(user, user.getPhoneVerificationCode()));
			return "authentication/phone-check";
		} catch (com.twilio.exception.ApiException e) {
			activateUser(user);
			if (user.isUsing2FA()) {
				redirectAttributes.addAttribute("qrCode", authenticationService.generateQRUrl(user));
				redirectAttributes.addAttribute("userName", user.getUserName());
				return "redirect:/qrCode?isPhoneNumberChecked=false";
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"There was a problem during the verification of your phone number. However, you can still use our services without any problems. Just note that you will not be notified by SMS when you receive or send bank transfers.");
				return "redirect:/login";
			}
		}
	}

	private void activateUser(User user) {
		user.setEnabled(true);
		userService.save(user);
	}

	@PostMapping("/confirmPhoneNumber")
	public String signupCheckPhoneNumber(@RequestParam("userName") String userName,
			@RequestParam(required = true, value = "code") String code, RedirectAttributes redirectAttributes)
			throws UserNotFoundException, UnsupportedEncodingException {
		User user = userService.findUserByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		final String phoneVerificationCode = user.getPhoneVerificationCode();
		if (phoneVerificationCode.equals(code)) {
			user.setUsingPhone(true);
			activateUser(user);
			if (user.isUsing2FA()) {
				redirectAttributes.addAttribute("qrCode", authenticationService.generateQRUrl(user));
				redirectAttributes.addAttribute("userName", user.getUserName());
				return "redirect:/qrCode";
			} else {
				redirectAttributes.addFlashAttribute("message", "Phone and account successfully verified");
				return "redirect:/login";
			}
		} else {
			redirectAttributes.addAttribute("errorMessage", "Wrong verification code");
			redirectAttributes.addAttribute("userName", user.getUserName());
			return "redirect:/phoneCheck";
		}
	}

	@RequestMapping("/updatePhoneNumber")
	public String getUpdatePhoneNumber(Model model, @RequestParam("userName") String userName)
			throws UserNotFoundException {
		User user = userService.findUserByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		model.addAttribute(user);
		return "authentication/new-phone-number";
	}

	@PostMapping("/updatePhoneNumber")
	public String postNewPhoneNumber(Model model, RedirectAttributes redirectAttributes,
			@RequestParam("userName") String userName, @RequestParam("phoneNumber") String phoneNumber)
			throws UserNotFoundException {

		if (!authenticationService.isPhoneNumberValid(phoneNumber)) {
			model.addAttribute("error", "Wrong phone number");
			return "authentication/new-phone-number";
		}

		authenticationService.updatePhoneNumber(phoneNumber, userName);
		redirectAttributes.addAttribute("userName", userName);
		redirectAttributes.addAttribute("message", "Phone number successfully updated");
		return "redirect:/phoneCheck";
	}

	// Reset registration

	@RequestMapping("/resetRegistration")
	public String getResetRegistration() {
		return "authentication/reset-registration";
	}

	@PostMapping("/resetRegistration")
	public String postResetRegistration(@RequestParam("email") final String userEmail,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		try {
			User user = userService.findUserByEmail(userEmail);
			final String appUrl = createAppUrl(request);
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
			redirectAttributes.addFlashAttribute("message", "You should receive a new activation email shortly");
			return "redirect:/login";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"It seems that this user does not exist. You must sign up first");
			return "redirect:/signup";
		}
	}

}
