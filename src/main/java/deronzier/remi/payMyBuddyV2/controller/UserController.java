package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.ConnectionNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	final static int OWNER_USER_ID = 1;

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
	public String getUser(@PathVariable final int id, Model model) {
		User user1 = userService.findById(OWNER_USER_ID).get();
		List<User> connections = user1.getConnections();
		User user = userService.findById(id).get();

		if (connections.contains(user) || user == user1) { // Check user is allowed to watch the profile of this
															// specific user
			model.addAttribute("user", user);
			return "users/profile";
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

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
