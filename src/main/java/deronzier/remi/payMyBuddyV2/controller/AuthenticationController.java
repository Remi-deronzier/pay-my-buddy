package deronzier.remi.payMyBuddyV2.controller;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import deronzier.remi.payMyBuddyV2.exception.UserEmailExistsException;
import deronzier.remi.payMyBuddyV2.exception.UserUserNameExistsException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.model.VerificationToken;
import deronzier.remi.payMyBuddyV2.registration.OnRegistrationCompleteEvent;
import deronzier.remi.payMyBuddyV2.service.UserService;

@Controller
public class AuthenticationController {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private UserService userService;

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

		try {
			final User registeredUser = userService.create(newUser);

			final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
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
			final RedirectAttributes redirectAttributes) {
		final VerificationToken verificationToken = userService.getVerificationToken(token);
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

		user.setEnabled(true);
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
		return new ModelAndView("redirect:/login");
	}

}
