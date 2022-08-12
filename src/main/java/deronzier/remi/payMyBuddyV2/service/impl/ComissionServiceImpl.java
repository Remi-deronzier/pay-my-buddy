package deronzier.remi.payMyBuddyV2.service.impl;

import java.time.LocalDateTime;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.model.BankTransferType;
import deronzier.remi.payMyBuddyV2.model.Commission;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.BankFlowRepository;
import deronzier.remi.payMyBuddyV2.repository.BankTransferRepository;
import deronzier.remi.payMyBuddyV2.repository.CommissionRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.CommissionService;

@Service
@Transactional
public class ComissionServiceImpl implements CommissionService {

	@Autowired
	private BankFlowRepository bankFlowRepository;

	@Autowired
	private BankTransferRepository bankTransferRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommissionRepository commissionRepository;

	@Override
	public void monetization() throws AccountNotFoundException, UserNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException {
		LocalDateTime now = LocalDateTime.now(); // current day for triggering the periodic task (HH:mm = 00:00)
		LocalDateTime startPreviousDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 1, 0, 0);
		LocalDateTime endPreviousDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);

		saveTotalDailyCommission(startPreviousDay, endPreviousDay);
		collectDailyCommissionForAllUsers(startPreviousDay, endPreviousDay);
	}

	private double calculateTotalDailyCommissionAmount(LocalDateTime startPreviousDay, LocalDateTime endPreviousDay) {
		Iterable<BankFlow> dailyBankFlows = bankFlowRepository.findByTimeStampBetween(startPreviousDay, endPreviousDay);
		return calculateDailyCommission(dailyBankFlows);
	}

	private double calculateDailyCommissionAmountForUser(LocalDateTime startPreviousDay, LocalDateTime endPreviousDay,
			int userId) {
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

	private void saveTotalDailyCommission(LocalDateTime startPreviousDay, LocalDateTime endPreviousDay) {
		double totalDailyCommission = calculateTotalDailyCommissionAmount(startPreviousDay, endPreviousDay);

		Commission dailyCommission = new Commission();
		dailyCommission.setAmount(totalDailyCommission);
		commissionRepository.save(dailyCommission);
	}

	private void collectDailyCommissionForAllUsers(LocalDateTime startPreviousDay, LocalDateTime endPreviousDay)
			throws UserNotFoundException,
			AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		Iterable<User> allUsers = userRepository.findAll();
		for (User user : allUsers) {
			collectDailyCommissionForOneUser(startPreviousDay, endPreviousDay, user.getId());
		}
	}

	private void collectDailyCommissionForOneUser(LocalDateTime startPreviousDay, LocalDateTime endPreviousDay,
			int userId) throws UserNotFoundException,
			AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		// Get user
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		// Get user's account
		Account userAccount = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		// Debit user of the commission amount
		double comissionAmount = calculateDailyCommissionAmountForUser(startPreviousDay, endPreviousDay, userId);
		userAccount.withdrawMoney(comissionAmount, true);

		// Update sent and received transactions for the sender and the receiver
		BankTransfer bankTransfer = new BankTransfer();
		bankTransfer.setExternalAccount(ExternalAccountServiceImpl.getPayMyBuddyAccount());
		bankTransfer.setAmount(comissionAmount);
		bankTransfer.setSender(user);
		bankTransfer.setBankTransferType(BankTransferType.USE);

		// Save data in DB
		bankTransferRepository.save(bankTransfer);
	}

}
