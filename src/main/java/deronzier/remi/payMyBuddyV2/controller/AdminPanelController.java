package deronzier.remi.paymybuddyv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import deronzier.remi.paymybuddyv2.model.Account;
import deronzier.remi.paymybuddyv2.service.AccountService;
import deronzier.remi.paymybuddyv2.utils.Constants;

@Controller
@RequestMapping(value = "/admin/adminPanel")
public class AdminPanelController {

	@Autowired
	private AccountService accountService;

	@GetMapping
	public String getAdminPanel(Model model) {
		// Add user account to model
		System.out.println("coucou");
		Account payMyBuddySuperUserAccount = accountService.findByUserId(Constants.PAY_MY_BUDDY_SUPER_USER_ID)
				.get();
		model.addAttribute("account", payMyBuddySuperUserAccount);

		return "admin-panel/view";
	}

}
