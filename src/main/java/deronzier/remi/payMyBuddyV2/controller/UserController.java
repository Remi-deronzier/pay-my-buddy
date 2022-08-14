package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import deronzier.remi.payMyBuddyV2.exception.IllegalPhoneNumberException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	public final static int OWNER_USER_ID = 2;

	@Autowired
	private UserServiceImpl userService;

	@GetMapping(value = "/contact")
	public String getConnections(Model model) {
		User user1 = userService.findById(OWNER_USER_ID).get();
		List<User> connections = user1.getConnections();
		model.addAttribute("connections", connections);
		return "users/contact";
	}

	@GetMapping(value = "/{id}")
	public String getUser(@PathVariable(required = false) final Integer id,
			@RequestParam(required = false) boolean isEditing, Model model,
			HttpServletRequest request) {
		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			IllegalPhoneNumberException illegalPhoneNumberException = (IllegalPhoneNumberException) inputFlashMap
					.get("illegalPhoneNumberException");
			DataIntegrityViolationException dataIntegrityViolationException = (DataIntegrityViolationException) inputFlashMap
					.get("dataIntegrityViolationException");
			model.addAttribute("dataIntegrityViolationException", dataIntegrityViolationException);
			model.addAttribute("illegalPhoneNumberException", illegalPhoneNumberException);
		}

		User user1 = userService.findById(OWNER_USER_ID).get();
		List<User> connections = user1.getConnections();
		User user = userService.findById(id).get();
		model.addAttribute("user", user);

		if (isEditing) {
			if (user == user1) { // Check user wants to edit his own profile
				return "users/editProfile";
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		} else {
			if (connections.contains(user) || user == user1) { // Check user is allowed to watch the profile of this
				// specific user
				return "users/profile";
			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}
	}

	@GetMapping(value = "/all")
	public String getAllUsers(Pageable pageable, Model model) {
		// Add all bank flows to model
		Page<User> users = userService
				.findAll(pageable);
		PageWrapper<User> page = new PageWrapper<User>(users, "/users/all");
		model.addAttribute("page", page);

		return "users/all";
	}

	@PatchMapping(value = "/{id}")
	public String updateProfile(@PathVariable final int id, @ModelAttribute("user") User user,
			RedirectAttributes redirectAttributes)
			throws UserNotFoundException {
		try {
			userService.updateProfile(user, id);
		} catch (IllegalPhoneNumberException ipne) {
			redirectAttributes.addFlashAttribute("illegalPhoneNumberException", ipne);
			return "redirect:/users/{id}?isProfileUpdatedSuccessfully=false&isEditing=true";
		} catch (DataIntegrityViolationException dive) {
			redirectAttributes.addFlashAttribute("dataIntegrityViolationException", dive);
			return "redirect:/users/{id}?isProfileUpdatedSuccessfully=false&isEditing=true";
		}
		return "redirect:/users/{id}?isProfileUpdatedSuccessfully=true";
	}

	@GetMapping(value = "/contact/add")
	public String addContactView(Model model) throws UserNotFoundException {
		User newConnection = new User();
		model.addAttribute("newConnection", newConnection);
		List<User> futurePotentialConnections = userService.findFuturePotentialConnections(OWNER_USER_ID);
		model.addAttribute("futurePotentialConnections", futurePotentialConnections);
		return "users/addContact";
	}

	@PostMapping("/contact/add")
	public String addConntactSendForm(@ModelAttribute("newConnection") User newConnection, Model model)
			throws UserNotFoundException, ConnectionCreationException {
		userService.addConnection(OWNER_USER_ID, newConnection.getId());
		return "redirect:/users/contact?isNewConnectionAddedSuccessfully=true";
	}

	@DeleteMapping("/contact/delete/{connectionId}")
	public String deleteContact(@PathVariable final int connectionId)
			throws UserNotFoundException, ConnectionCreationException, ConnectionNotFoundException {
		userService.deleteConnection(OWNER_USER_ID, connectionId);
		return "redirect:/users/contact?isConnectionDeletedSuccessfully=true";
	}

}
