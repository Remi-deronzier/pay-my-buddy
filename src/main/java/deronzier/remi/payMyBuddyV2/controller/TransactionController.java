package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;

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

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.ConnectionCreationException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.TransactionServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;
import deronzier.remi.payMyBuddyV2.utils.PageWrapper;

@Controller
@RequestMapping(value = "/transactions")
public class TransactionController {

	@Autowired
	private TransactionServiceImpl transactionService;

	@Autowired
	private UserServiceImpl userService;

	@GetMapping()
	public String getTransactions(Model model,
			@SortDefault(sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable)
			throws UserNotFoundException {
		// Create empty user for the transaction form
		User receiver = new User();
		model.addAttribute("receiver", receiver);

		// Create empty transaction for the transaction form
		Transaction newTransaction = new Transaction();
		model.addAttribute("newTransaction", newTransaction);

		// Get current user's connections
		User user1 = userService.findById(UserController.OWNER_USER_ID).get();
		List<User> connections = user1.getConnections();
		model.addAttribute("connections", connections);

		// Get all current user's transactions
		Page<Transaction> transactions = transactionService
				.findAllSentAndReceivedTransactionsForSpecificUser(UserController.OWNER_USER_ID, pageable);
		PageWrapper<Transaction> page = new PageWrapper<Transaction>(transactions, "/transactions");
		model.addAttribute("page", page);

		return "transactions/view";
	}

	@PostMapping("/makeTransaction")
	public String makeTransaction(@ModelAttribute Transaction transaction, @ModelAttribute User receiver,
			Model model)
			throws UserNotFoundException, ConnectionCreationException, AccountNotFoundException,
			NegativeAmountException, AccountNotEnoughMoney, TransactionSameAccountException {
		transactionService.makeTransaction(UserController.OWNER_USER_ID, receiver.getId(), transaction.getAmount(),
				transaction.getDescription());
		return "redirect:/transactions?isNewTransactionMadeSuccessfully=true";
	}

}
