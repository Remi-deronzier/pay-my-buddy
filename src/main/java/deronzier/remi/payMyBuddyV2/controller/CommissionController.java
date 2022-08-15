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

import deronzier.remi.payMyBuddyV2.model.Commission;
import deronzier.remi.payMyBuddyV2.service.CommissionService;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/commissions")
public class CommissionController {

	@Autowired
	private CommissionService commissionService;

	@GetMapping
	public String getCommissions(Model model,
			@SortDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

		// Add all bank flows to model
		Page<Commission> commissions = commissionService
				.findAll(pageable);
		PageWrapper<Commission> page = new PageWrapper<Commission>(commissions, "/commissions");
		model.addAttribute("page", page);

		return "commissions/view";
	}

}
