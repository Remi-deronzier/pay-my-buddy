package deronzier.remi.payMyBuddyV2.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import deronzier.remi.payMyBuddyV2.exception.UserEmailExistsException;
import deronzier.remi.payMyBuddyV2.exception.UserUserNameExistsException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.UserService;

@Controller
public class AuthenticationController {

	@Autowired
	private UserService userService;

	@RequestMapping("/login")
	public String login() {
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
			Model model) {
		if (result.hasErrors()) {
			return returnSignupPageWithErrorMessages(model, newUser);
		}

		try {
			userService.create(newUser);
		} catch (UserEmailExistsException ueee) {
			result.addError(new FieldError("newUser", "email", ueee.getMessage()));
			return returnSignupPageWithErrorMessages(model, newUser);
		} catch (UserUserNameExistsException uunee) {
			result.addError(new FieldError("newUser", "userName", uunee.getMessage()));
			return returnSignupPageWithErrorMessages(model, newUser);
		}

		return "redirect:/authentication/signup-view";
	}

	private String returnSignupPageWithErrorMessages(Model model, User newUser) {
		model.addAttribute("newUser", newUser);
		model.addAttribute("hasCustomErrors", true);
		return "authentication/signup-view";
	}

}
