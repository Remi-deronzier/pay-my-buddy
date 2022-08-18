package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
import deronzier.remi.payMyBuddyV2.security.CustomUser;
import deronzier.remi.payMyBuddyV2.service.BankTransferService;
import deronzier.remi.payMyBuddyV2.service.UserService;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@ControllerAdvice
@Controller
@RequestMapping(value = "/bankTransfers")
public class BankTransferController {

	@Autowired
	private BankTransferService bankTransferService;

	@Autowired
	private UserService userService;

	@GetMapping()
	public String getBankTransfers(Model model, HttpServletRequest request,
			@AuthenticationPrincipal CustomUser customUser,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable)
			throws UserNotFoundException {
		final int userId = customUser.getId();

		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			AccountNotEnoughMoneyException accountNotEnoughMoneyException = (AccountNotEnoughMoneyException) inputFlashMap
					.get("accountNotEnoughMoneyException");
			NegativeAmountException negativeAmountExceptionTopUp = (NegativeAmountException) inputFlashMap
					.get("negativeAmountExceptionTopUp");
			NegativeAmountException negativeAmountExceptionUse = (NegativeAmountException) inputFlashMap
					.get("negativeAmountExceptionUse");
			String tooLowAmountErrorTopUp = (String) inputFlashMap.get("tooLowAmountErrorTopUp");
			String tooLowAmountErrorUse = (String) inputFlashMap.get("tooLowAmountErrorUse");
			model.addAttribute("tooLowAmountErrorUse", tooLowAmountErrorUse);
			model.addAttribute("tooLowAmountErrorTopUp", tooLowAmountErrorTopUp);
			model.addAttribute("accountNotEnoughMoneyException", accountNotEnoughMoneyException);
			model.addAttribute("negativeAmountExceptionTopUp", negativeAmountExceptionTopUp);
			model.addAttribute("negativeAmountExceptionUse", negativeAmountExceptionUse);
		}

		// Add external accounts of the user
		User owner = userService.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		List<ExternalAccount> externalAccounts = owner.getExternalAccounts();
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
				.findAllBankTransfersForSpecificUser(userId, pageable);
		PageWrapper<BankTransfer> page = new PageWrapper<BankTransfer>(bankTransfers, "/bankTransfers");
		model.addAttribute("page", page);

		return "bank-transfers/view";
	}

	@PostMapping("/makeBankTransfer")
	public String makeBankTransfer(
			@Valid @ModelAttribute(value = "newBankTransferUse") BankTransfer newBankTransfer,
			@AuthenticationPrincipal CustomUser customUser,
			BindingResult bindingResultBankTransfer,
			@ModelAttribute("externalAccountUse") ExternalAccount externalAccount,
			Model model, @RequestParam(value = "bankTransferType", required = true) BankTransferType bankTransferType,
			RedirectAttributes redirectAttributes)
			throws UserNotFoundException, AccountNotFoundException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		final int userId = customUser.getId();

		try {
			switch (bankTransferType) {
			case TOP_UP:
				if (bindingResultBankTransfer.hasErrors()) { // if amount is lower than 10€ for Use form
					redirectAttributes.addFlashAttribute("tooLowAmountErrorTopUp",
							bindingResultBankTransfer.getFieldError("amount").getDefaultMessage());
					return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=false";
				}
				try {
					bankTransferService.makeBankTransfer(newBankTransfer.getAmount(), userId,
							BankTransferType.TOP_UP,
							externalAccount.getId());
				} catch (NegativeAmountException nae) {
					redirectAttributes.addFlashAttribute("negativeAmountExceptionTopUp", nae);
					return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=false";
				}
				break;
			case USE:
				if (bindingResultBankTransfer.hasErrors()) { // if amount is lower than 10€ for Use form
					redirectAttributes.addFlashAttribute("tooLowAmountErrorUse",
							bindingResultBankTransfer.getFieldError("amount").getDefaultMessage());
					return "redirect:/bankTransfers?isNewBankTransferMadeSuccessfully=false";
				}
				try {
					bankTransferService.makeBankTransfer(newBankTransfer.getAmount(), userId,
							BankTransferType.USE,
							externalAccount.getId());
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
