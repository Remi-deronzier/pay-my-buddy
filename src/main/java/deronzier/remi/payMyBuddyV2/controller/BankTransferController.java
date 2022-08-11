package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.model.BankTransferType;
import deronzier.remi.payMyBuddyV2.model.ExternalAccount;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.BankTransferServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/bankTransfers")
public class BankTransferController {

	@Autowired
	private BankTransferServiceImpl bankTransferService;

	@Autowired
	private UserServiceImpl userService;

	@GetMapping()
	public String getBankTransfers(Model model, HttpServletRequest request,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable) {
		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			AccountNotEnoughMoneyException accountNotEnoughMoneyException = (AccountNotEnoughMoneyException) inputFlashMap
					.get("accountNotEnoughMoneyException");
			NegativeAmountException negativeAmountExceptionTopUp = (NegativeAmountException) inputFlashMap
					.get("negativeAmountExceptionTopUp");
			NegativeAmountException negativeAmountExceptionUse = (NegativeAmountException) inputFlashMap
					.get("negativeAmountExceptionUse");
			model.addAttribute("accountNotEnoughMoneyException", accountNotEnoughMoneyException);
			model.addAttribute("negativeAmountExceptionTopUp", negativeAmountExceptionTopUp);
			model.addAttribute("negativeAmountExceptionUse", negativeAmountExceptionUse);
		}

		// Add external accounts of the user
		User user1 = userService.findById(UserController.OWNER_USER_ID).get();
		List<ExternalAccount> externalAccounts = user1.getExternalAccounts();
		model.addAttribute("externalAccounts", externalAccounts);

		// Create empty bank transfer for use for the bank transfer of type use
		BankTransfer newBankTransferUse = new BankTransfer();
		model.addAttribute("newBankTransferUse", newBankTransferUse);

		// Create empty bank transfer for top up for the bank transfer of type top up
		BankTransfer newBankTransferTopUp = new BankTransfer();
		model.addAttribute("newBankTransferTopUp", newBankTransferTopUp);

		// Create empty external account for top up for the bank transfer of type top up
		ExternalAccount externalAccountTopUp = new ExternalAccount();
		model.addAttribute("externalAccountTopUp", externalAccountTopUp);

		// Create empty external account for use for the bank transfer of type top up
		ExternalAccount externalAccountUse = new ExternalAccount();
		model.addAttribute("externalAccountUse", externalAccountUse);

		// Get all current user's transactions
		Page<BankTransfer> bankTransfers = bankTransferService
				.findAllBankTransfersForSpecificUser(UserController.OWNER_USER_ID, pageable);
		PageWrapper<BankTransfer> page = new PageWrapper<BankTransfer>(bankTransfers, "/bankTransfers");
		model.addAttribute("page", page);

		return "bank-transfers/view";
	}

	@PostMapping("/makeBankTransfer")
	public String makeBankTransfer(@ModelAttribute("newBankTransferTopUp") BankTransfer newBankTransferTopUp,
			@ModelAttribute("newBankTransferUse") BankTransfer newBankTransferUse,
			@ModelAttribute("externalAccountTopUp") ExternalAccount externalAccountTopUp,
			@ModelAttribute("externalAccountUse") ExternalAccount externalAccountUse,
			Model model, @RequestParam(value = "bankTransferType", required = true) BankTransferType bankTransferType,
			RedirectAttributes redirectAttributes)
			throws UserNotFoundException, AccountNotFoundException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		try {
			switch (bankTransferType) {
			case TOP_UP:
				try {
					bankTransferService.makeBankTransfer(newBankTransferTopUp.getAmount(), UserController.OWNER_USER_ID,
							BankTransferType.TOP_UP,
							externalAccountTopUp.getId());
				} catch (NegativeAmountException nae) {
					redirectAttributes.addFlashAttribute("negativeAmountExceptionTopUp", nae);
					return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=false";
				}
				break;
			case USE:
				try {
					bankTransferService.makeBankTransfer(newBankTransferTopUp.getAmount(), UserController.OWNER_USER_ID,
							BankTransferType.USE,
							externalAccountTopUp.getId());
				} catch (NegativeAmountException nae) {
					redirectAttributes.addFlashAttribute("negativeAmountExceptionUse", nae);
					return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=false";
				}
				break;
			}
			return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=true";
		} catch (AccountNotEnoughMoneyException aneme) {
			redirectAttributes.addFlashAttribute("accountNotEnoughMoneyException", aneme);
			return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=false";
		}
	}
}
