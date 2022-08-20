package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import deronzier.remi.payMyBuddyV2.event.bankFlow.OnBankFlowCompleteEvent;
import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.security.CustomUser;
import deronzier.remi.payMyBuddyV2.service.TransactionService;
import deronzier.remi.payMyBuddyV2.service.UserService;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping()
	public String getTransactions(Model model, HttpServletRequest request,
			@AuthenticationPrincipal CustomUser customUser,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable)
			throws UserNotFoundException {
		final int userId = customUser.getId();

		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			AccountNotEnoughMoneyException accountNotEnoughMoneyException = (AccountNotEnoughMoneyException) inputFlashMap
					.get("accountNotEnoughMoneyException");
			NegativeAmountException negativeAmountException = (NegativeAmountException) inputFlashMap
					.get("negativeAmountException");
			String tooLowAmountError = (String) inputFlashMap.get("tooLowAmountError");
			model.addAttribute("accountNotEnoughMoneyException", accountNotEnoughMoneyException);
			model.addAttribute("negativeAmountException", negativeAmountException);
			model.addAttribute("tooLowAmountError", tooLowAmountError);
		}

		// Create empty user for the transaction form
		User receiver = new User();
		model.addAttribute("receiver", receiver);

		// Create empty transaction for the transaction form
		Transaction newTransaction = new Transaction();
		model.addAttribute("newTransaction", newTransaction);

		// Get current user's connections
		User user = userService.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		List<User> connections = user.getConnections();
		model.addAttribute("connections", connections);

		// Get all current user's transactions
		Page<Transaction> transactions = transactionService
				.findAllSentAndReceivedTransactionsForSpecificUser(userId, pageable);
		PageWrapper<Transaction> page = new PageWrapper<Transaction>(transactions, "/transactions");
		model.addAttribute("page", page);

		return "transactions/view";
	}

	@PostMapping("/makeTransaction")
	public String makeTransaction(@Valid @ModelAttribute("newTransaction") Transaction transaction,
			BindingResult bindingResult, @AuthenticationPrincipal CustomUser customUser,
			@ModelAttribute("receiver") User receiver, Model model,
			RedirectAttributes redirectAttributes)
			throws UserNotFoundException, AccountNotFoundException, TransactionSameAccountException {
		final int userId = customUser.getId();
		User userLoggedIn = userService.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		if (bindingResult.hasErrors()) { // if amount is lower than 10€
			redirectAttributes.addFlashAttribute("tooLowAmountError",
					bindingResult.getFieldError("amount").getDefaultMessage());
			return "redirect:/transactions?isNewTransactionMadeSuccessfully=false";
		}
		try {
			transactionService.makeTransaction(userId, receiver.getId(), transaction.getAmount(),
					transaction.getDescription());
			if (userLoggedIn.getPhoneNumber() != null && !userLoggedIn.getPhoneNumber().isEmpty()) {
				eventPublisher.publishEvent(new OnBankFlowCompleteEvent(userLoggedIn, transaction.getAmount()));
			}
			return "redirect:/transactions?isNewTransactionMadeSuccessfully=true";
		} catch (AccountNotEnoughMoneyException aneme) {
			redirectAttributes.addFlashAttribute("accountNotEnoughMoneyException", aneme);
			return "redirect:/transactions?isNewTransactionMadeSuccessfully=false";
		} catch (NegativeAmountException nae) {
			redirectAttributes.addFlashAttribute("negativeAmountException", nae);
			return "redirect:/transactions?isNewTransactionMadeSuccessfully=false";
		}
	}

}
