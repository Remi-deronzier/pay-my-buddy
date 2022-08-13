package deronzier.remi.payMyBuddyV2.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.model.Commission;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.BankFlowRepository;
import deronzier.remi.payMyBuddyV2.repository.CommissionRepository;
import deronzier.remi.payMyBuddyV2.repository.TransactionRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.CommissionService;
import deronzier.remi.payMyBuddyV2.service.UserService;

@Service
@Transactional
public class CommissionServiceImpl implements CommissionService {

	@Autowired
	private BankFlowRepository bankFlowRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommissionRepository commissionRepository;

	final LocalDateTime now = LocalDateTime.now(); // current day for triggering the periodic task (HH:mm = 00:00)
	final LocalDateTime startPreviousDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 1, 0,
			0);
	final LocalDateTime endPreviousDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);

	@Override
	public double monetization() throws AccountNotFoundException, UserNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException {
		collectDailyCommissionForAllUsers();
		return saveTotalDailyCommission();
	}

	private double calculateTotalDailyCommissionAmount() {
		Iterable<BankFlow> dailyBankFlows = bankFlowRepository.findByTimeStampBetween(startPreviousDay, endPreviousDay);
		return calculateDailyCommission(dailyBankFlows);
	}

	private double calculateDailyCommissionAmountForUser(int userId) {
		Iterable<BankFlow> dailyBankFlows = bankFlowRepository
				.findByTimeStampBetweenAndSenderId(startPreviousDay, endPreviousDay, userId);
		return calculateDailyCommission(dailyBankFlows);
	}

	private double calculateDailyCommission(Iterable<BankFlow> dailyBankFlows) {
		double dailyCommissionAmount = 0;
		for (BankFlow dailyBankFlow : dailyBankFlows) {
			dailyCommissionAmount += dailyBankFlow.getAmount() * CommissionService.COMMISSION_PERCENTAGE / 100;
		}
		return dailyCommissionAmount;
	}

	private double saveTotalDailyCommission() {
		double totalDailyCommission = calculateTotalDailyCommissionAmount();
		Commission dailyCommission = new Commission();
		dailyCommission.setAmount(totalDailyCommission);
		commissionRepository.save(dailyCommission);
		return totalDailyCommission;
	}

	private void collectDailyCommissionForAllUsers()
			throws UserNotFoundException,
			AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		Iterable<User> allUsers = userRepository.findAll();
		for (User user : allUsers) {
			collectDailyCommissionForOneUser(user.getId());
		}
	}

	private void collectDailyCommissionForOneUser(int userId) throws UserNotFoundException,
			AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		// Get users
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		User payMyBuddySuperUser = userRepository.findById(UserService.PAY_MY_BUDDY_SUPER_USER_ID)
				.orElseThrow(() -> new UserNotFoundException("Pay My Buddy Super user not found"));

		// Get accounts
		Account userAccount = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		Account payMyBuddySuperUserAccount = accountRepository.findByUserId(UserService.PAY_MY_BUDDY_SUPER_USER_ID)
				.orElseThrow(() -> new AccountNotFoundException("Pay My Buddy Super user account not found"));

		double comissionAmount = calculateDailyCommissionAmountForUser(userId);

		// Debit user of the commission amount
		userAccount.withdrawMoney(comissionAmount, true);

		// Credit Pay My Buddy Super User account of the commission amount
		payMyBuddySuperUserAccount.addMoney(comissionAmount);

		// Update sent and received transactions for the sender and the receiver
		Transaction transaction = new Transaction();
		try {
			transaction.setSender(user);
			transaction.setReceiver(payMyBuddySuperUser);
			transaction.setAmount(comissionAmount);
			transaction.setDescription("Daily fee for " + LocalDate.now().minusDays(1));
			transactionRepository.save(transaction);
		} catch (ConstraintViolationException cve) { // when commission amount is smaller than 10€
			// Save data in DB
			transactionRepository.save(transaction);
		}
	}

}
