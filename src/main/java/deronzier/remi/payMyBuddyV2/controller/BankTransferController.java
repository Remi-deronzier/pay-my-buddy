package deronzier.remi.payMyBuddyV2.controller;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.service.impl.BankTransferServiceImpl;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/bankTransfers")
public class BankTransferController {

	@Autowired
	private BankTransferServiceImpl bankTransferService;

	@GetMapping()
	public String getBankTransfers(Model model,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable) {
		// Create empty bank transfer for use for the bank transfer of type use
		BankTransfer newBankTransferUse = new BankTransfer();
		model.addAttribute("newBankTransferUse", newBankTransferUse);

		// Create empty bank transfer for top up for the bank transfer of type top up
		BankTransfer newBankTransferTopUp = new BankTransfer();
		model.addAttribute("newBankTransferTopUp", newBankTransferTopUp);

		// Get all current user's transactions
		Page<BankTransfer> bankTransfers = bankTransferService
				.findAllBankTransfersForSpecificUser(UserController.OWNER_USER_ID, pageable);
		PageWrapper<BankTransfer> page = new PageWrapper<BankTransfer>(bankTransfers, "/bankTransfers");
		model.addAttribute("page", page);

		return "bank-transfers/view";
	}

	@PostMapping("/makeBankTransfer")
	public String makeBankTransfer(@ModelAttribute BankTransfer newBankTransferTopUp,
			@ModelAttribute BankTransfer newBankTransferUse,
			Model model, @RequestParam(value = "bankTransferType", required = true) String bankTransferType)
			throws UserNotFoundException, ConnectionCreationException, AccountNotFoundException,
			NegativeAmountException, AccountNotEnoughMoney, TransactionSameAccountException {
		System.out.println(bankTransferType);
		System.out.println(bankTransferType.equals("topUp"));
		if (bankTransferType.equals("topUp")) {
			bankTransferService.makeBankTransfer(newBankTransferTopUp.getAmount(), UserController.OWNER_USER_ID, true);
		}
		if (bankTransferType.equals("use")) {
			bankTransferService.makeBankTransfer(newBankTransferUse.getAmount(), UserController.OWNER_USER_ID, false);
		}
		return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=true";
	}
}
