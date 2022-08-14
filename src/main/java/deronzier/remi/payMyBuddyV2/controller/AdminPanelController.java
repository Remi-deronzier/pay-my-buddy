package deronzier.remi.payMyBuddyV2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.service.impl.AccountServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;

@Controller
@RequestMapping(value = "/adminPanel")
public class AdminPanelController {

	@Autowired
	private AccountServiceImpl accountService;

	@GetMapping
	public String getAdminPanel(Model model) {
		// Add user account to model
		Account payMyBuddySuperUserAccount = accountService.findByUserId(UserServiceImpl.PAY_MY_BUDDY_SUPER_USER_ID)
				.get();
		model.addAttribute("account", payMyBuddySuperUserAccount);

		return "admin-panel/view";
	}

}
