package deronzier.remi.payMyBuddyV2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.service.BankFlowService;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/admin/bankFlows")
public class BankFlowController {

	@Autowired
	private BankFlowService bankFlowService;

	@GetMapping
	public String getBankFlows(Model model,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable) {

		// Add all bank flows to model
		Page<BankFlow> bankFlows = bankFlowService
				.findAll(pageable);
		PageWrapper<BankFlow> page = new PageWrapper<BankFlow>(bankFlows, "/bankFlows");
		model.addAttribute("page", page);

		return "bank-flows/view";
	}

}
