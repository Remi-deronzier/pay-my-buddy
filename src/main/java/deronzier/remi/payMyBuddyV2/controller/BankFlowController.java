package deronzier.remi.paymybuddyv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import deronzier.remi.paymybuddyv2.model.BankFlow;
import deronzier.remi.paymybuddyv2.service.BankFlowService;
import deronzier.remi.paymybuddyv2.utils.PageWrapper;
import deronzier.remi.paymybuddyv2.utils.RouteManager;

/**
 * @author remax Just an example to show how a proper controller should be if we
 *         want to have const route names to avoid typo
 *
 */
@Controller
@RequestMapping(value = RouteManager.ADMIN + RouteManager.BANK_FLOWS)
public class BankFlowController {

	@Autowired
	private BankFlowService bankFlowService;

	@GetMapping
	public String getBankFlows(Model model,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable) {

		// Add all bank flows to model
		Page<BankFlow> bankFlows = bankFlowService
				.findAll(pageable);
		PageWrapper<BankFlow> page = new PageWrapper<BankFlow>(bankFlows, RouteManager.BANK_FLOWS);
		model.addAttribute("page", page);

		return "bank-flows/view";
	}

}
