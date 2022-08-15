package deronzier.remi.payMyBuddyV2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.service.AccountService;
import deronzier.remi.payMyBuddyV2.service.BankFlowService;
import deronzier.remi.payMyBuddyV2.utils.Constants;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
public class HomeController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private BankFlowService bankFlowService;

	@GetMapping
	public String getHome(Model model,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable) {
		// Add user account to model
		Account account = accountService.findByUserId(Constants.OWNER_USER_ID).get();
		model.addAttribute("account", account);

		// Add all bank flows to model
		Page<BankFlow> bankFlows = bankFlowService
				.findAllBankFlowsForSpecificUser(Constants.OWNER_USER_ID, pageable);
		PageWrapper<BankFlow> page = new PageWrapper<BankFlow>(bankFlows, "/");
		model.addAttribute("page", page);

		return "home";
	}

}
