package deronzier.remi.payMyBuddyV2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.service.impl.AccountServiceImpl;

@Controller
public class GeneralController {

	@Autowired
	private AccountServiceImpl accountService;

	@GetMapping(value = "/")
	public String getConnections(Model model) {
		Account account = accountService.findByUserId(UserController.OWNER_USER_ID).get();
		model.addAttribute("account", account);
		return "home";
	}

}
