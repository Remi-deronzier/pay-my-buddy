package deronzier.remi.paymybuddyv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import deronzier.remi.paymybuddyv2.exception.AccountNotFoundException;
import deronzier.remi.paymybuddyv2.model.Account;
import deronzier.remi.paymybuddyv2.model.BankFlow;
import deronzier.remi.paymybuddyv2.security.CustomUser;
import deronzier.remi.paymybuddyv2.service.AccountService;
import deronzier.remi.paymybuddyv2.service.BankFlowService;
import deronzier.remi.paymybuddyv2.utils.PageWrapper;

@Controller
public class HomeController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private BankFlowService bankFlowService;

	@GetMapping
	public String getHome(Model model, @AuthenticationPrincipal CustomUser customUser, Authentication principal,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable)
			throws AccountNotFoundException {
		final int userId = customUser.getId();

		// Add user account to model
		Account account = accountService.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		model.addAttribute("account", account);

		// Add all bank flows to model
		Page<BankFlow> bankFlows = bankFlowService
				.findAllSentAndReceivedBankFlowsForSpecificUser(userId, pageable);
		PageWrapper<BankFlow> page = new PageWrapper<BankFlow>(bankFlows, "/");
		model.addAttribute("page", page);

		return "home";
	}

}
