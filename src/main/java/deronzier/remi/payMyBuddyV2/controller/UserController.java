package deronzier.remi.payMyBuddyV2.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.ConnectionNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.security.CustomUser;
import deronzier.remi.payMyBuddyV2.service.UserService;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/contact")
	public String getConnections(Model model, @AuthenticationPrincipal CustomUser customUser)
			throws UserNotFoundException {
		final int userId = customUser.getId();

		User user = userService.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		List<User> connections = user.getConnections();
		model.addAttribute("connections", connections);
		return "users/contact";
	}

	@GetMapping(value = "/{userName}")
	public String getUser(@PathVariable(required = false) final String userName,
			@RequestParam(required = false) boolean isEditing, Model model,
			HttpServletRequest request, @AuthenticationPrincipal CustomUser customUser) throws UserNotFoundException {
		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			DataIntegrityViolationException dataIntegrityViolationException = (DataIntegrityViolationException) inputFlashMap
					.get("dataIntegrityViolationException");
			model.addAttribute("dataIntegrityViolationException", dataIntegrityViolationException);
		}

		User userLoggedIn = userService.findUserByUsername(customUser.getUsername())
				.orElseThrow(() -> new UserNotFoundException("User logged in not found"));
		User user = userService.findUserByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		List<User> connections = userLoggedIn.getConnections();
		model.addAttribute("user", user);

		if (isEditing) {
			if (user == userLoggedIn) { // Check user wants to edit his own profile
				return "users/editProfile";
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		} else {
			if (connections.contains(user)
					|| user == userLoggedIn) { // Check user is allowed to watch the profile
												// of this
				// specific user
				return "users/profile";
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}
	}

	@GetMapping(value = "/admin/all")
	public String getUsers(Pageable pageable, Model model) {
		// Add all bank flows to model
		Page<User> users = userService
				.findAll(pageable);
		PageWrapper<User> page = new PageWrapper<User>(users, "/users/all");
		model.addAttribute("page", page);

		return "users/all";
	}

	@PatchMapping(value = "/{id}")
	public String updateProfile(@PathVariable final int id, @ModelAttribute("user") User user,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUser customUser)
			throws UserNotFoundException {
		if (id == customUser.getId()) { // Check that only logged in user can change his profile
			final String userName = customUser.getUsername();
			try {
				userService.updateProfile(user, id);
			} catch (DataIntegrityViolationException dive) {
				redirectAttributes.addFlashAttribute("dataIntegrityViolationException", dive);
				return "redirect:/users/" + userName + "?isProfileUpdatedSuccessfully=false&isEditing=true";
			}
			return "redirect:/users/" + userName + "?isProfileUpdatedSuccessfully=true";
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping(value = "/contact/add")
	public String getAddContact(Model model, @AuthenticationPrincipal CustomUser customUser)
			throws UserNotFoundException {
		final int userId = customUser.getId();
		User newConnection = new User();
		model.addAttribute("newConnection", newConnection);
		List<User> futurePotentialConnections = userService.findFuturePotentialConnections(userId);
		model.addAttribute("futurePotentialConnections", futurePotentialConnections);
		return "users/addContact";
	}

	@PostMapping("/contact/add")
	public String postAddContact(@ModelAttribute("newConnection") User newConnection, Model model,
			@AuthenticationPrincipal CustomUser customUser)
			throws UserNotFoundException, ConnectionCreationException {
		final int userId = customUser.getId();
		userService.addConnection(userId, newConnection.getId());
		return "redirect:/users/contact?isNewConnectionAddedSuccessfully=true";
	}

	@GetMapping(value = "/settings/{userName}")
	public String getSettings(Model model, @PathVariable final String userName)
			throws UserNotFoundException {
		User user = userService.findUserByUsername(userName)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		final int userId = user.getId();
		model.addAttribute("using2FA", user.isUsing2FA());
		model.addAttribute("userId", userId);
		model.addAttribute("userName", userName);
		return "users/settings";
	}

	@PatchMapping("/settings/{id}")
	public String patchSettings(@RequestParam(required = true, value = "using2FA") boolean using2FA,
			@PathVariable final int id, @RequestParam(required = true, value = "userName") String userName,
			RedirectAttributes redirectAttributes)
			throws UserNotFoundException, UnsupportedEncodingException {
		User user = userService.findUserById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		userService.updateUsing2FA(using2FA, id);
		if (using2FA) {
			redirectAttributes.addAttribute("qrCode", userService.generateQRUrl(user));
			redirectAttributes.addAttribute("userName", userName);
			return "redirect:/qrCode";
		} else {
			return "redirect:/users/" + userName + "?areSettingsUpdatedSuccessfully=true";
		}
	}

	@DeleteMapping("/contact/delete/{connectionId}")
	public String deleteContact(@PathVariable final int connectionId, @AuthenticationPrincipal CustomUser customUser)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException {
		final int userId = customUser.getId();
		userService.deleteConnection(userId, connectionId);
		return "redirect:/users/contact?isConnectionDeletedSuccessfully=true";
	}

}
