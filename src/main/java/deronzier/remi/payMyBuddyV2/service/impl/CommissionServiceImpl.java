package deronzier.remi.payMyBuddyV2.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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

	private static final Logger log = LoggerFactory.getLogger(CommissionServiceImpl.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");

	@Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris") // every day at midnight
	@Override
	public double monetization() throws AccountNotFoundException, UserNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException {
		log.info("Calculation of the commission for {} launched at {}", startPreviousDay,
				dateFormat.format(new Date()));
		collectDailyCommissionForAllUsers();
		return saveTotalDailyCommission();
	}

	private double calculateTotalDailyCommissionAmount() {
		Iterable<BankFlow> dailyBankFlows = bankFlowRepository
				.findByTimeStampGreaterThanEqualAndTimeStampLessThan(startPreviousDay, endPreviousDay);
		return calculateDailyCommission(dailyBankFlows);
	}

	private double calculateDailyCommissionAmountForUser(int userId) {
		Iterable<BankFlow> dailyBankFlows = bankFlowRepository
				.findByTimeStampGreaterThanEqualAndTimeStampLessThanAndSenderId(startPreviousDay,
						endPreviousDay,
						userId);
		return calculateDailyCommission(dailyBankFlows);
	}

	private double calculateDailyCommission(Iterable<BankFlow> dailyBankFlows) {
		double dailyCommissionAmount = 0;
		for (BankFlow dailyBankFlow : dailyBankFlows) {
			if (dailyBankFlow.getDiscriminatorValue().equals("transaction")) {
				if (((Transaction) dailyBankFlow).getReceiver().getId() != UserService.PAY_MY_BUDDY_SUPER_USER_ID) {
					// Avoid counting bank transfers charged by Pay My Buddy
					dailyCommissionAmount += calculateCommissionFee(dailyBankFlow.getAmount());
				}
			} else {
				dailyCommissionAmount += calculateCommissionFee(dailyBankFlow.getAmount());
			}
		}
		return dailyCommissionAmount;
	}

	private double calculateCommissionFee(double amount) {
		return amount * CommissionService.COMMISSION_PERCENTAGE / 100;
	}

	private double saveTotalDailyCommission() throws AccountNotFoundException, NegativeAmountException {
		Account payMyBuddySuperUserAccount = accountRepository.findByUserId(UserService.PAY_MY_BUDDY_SUPER_USER_ID)
				.orElseThrow(() -> new AccountNotFoundException("Pay My Buddy Super user account not found"));

		double totalDailyCommission = calculateTotalDailyCommissionAmount();
		Commission dailyCommission = new Commission();
		if (totalDailyCommission > 0) { // Only if strictly positive
			dailyCommission.setAmount(totalDailyCommission);
			payMyBuddySuperUserAccount.addMoney(totalDailyCommission);
			commissionRepository.save(dailyCommission);
			return totalDailyCommission;
		} else {
			return 0;
		}

	}

	private void collectDailyCommissionForAllUsers()
			throws UserNotFoundException,
			AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		Iterable<User> allUsers = userRepository.findAll();
		User payMyBuddySuperUser = userRepository.findById(UserService.PAY_MY_BUDDY_SUPER_USER_ID)
				.orElseThrow(() -> new UserNotFoundException("Pay My Buddy Super user not found"));
		for (User user : allUsers) {
			if (user.getId() > UserService.PAY_MY_BUDDY_SUPER_USER_ID) { // Skip Pay My Buddy Super User
				collectDailyCommissionForOneUser(user.getId(), payMyBuddySuperUser);
			}
		}
	}

	private void collectDailyCommissionForOneUser(int userId, User payMyBuddySuperUser) throws UserNotFoundException,
			AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		// Get users
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		// Get accounts
		Account userAccount = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		double commissionAmount = calculateDailyCommissionAmountForUser(userId);

		if (commissionAmount > 0) { // Only if commission if strictly positive
			// Debit user of the commission amount
			userAccount.withdrawMoney(commissionAmount, true);

			// Update sent and received transactions for the sender and the receiver
			Transaction transaction = new Transaction();
			transaction.setSender(user);
			transaction.setReceiver(payMyBuddySuperUser);
			transaction.setAmount(commissionAmount);
			transaction.setDescription("Daily fee for " + LocalDate.now().minusDays(1));

			// Save
			transactionRepository.save(transaction);
		}
	}

	@Override
	public Page<Commission> findAll(Pageable pageable) {
		return commissionRepository.findAll(pageable);
	}

}
