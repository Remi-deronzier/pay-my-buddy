package deronzier.remi.payMyBuddyV2.service.impl;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.BankTransferRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BankTransferRepository bankTransferRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<Account> findByUserId(int userId) {
		return accountRepository.findByUserId(userId);
	}

	public Account addMoney(double amount, int userId)
			throws NegativeAmountException, AccountNotFoundException, UserNotFoundException {
		// Check if account and user exist
		Account account = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

		// Credit the user's account
		account.addMoney(amount);

		// Save transfer in bank transfer table
		BankTransfer bankTransfer = new BankTransfer(amount);
		user.addBankTransfer(bankTransfer);
		bankTransferRepository.save(bankTransfer);

		// Save user's account with the new balance
		return accountRepository.save(account);
	}

	public Account withdrawMoney(double amount, int userId)
			throws NegativeAmountException, AccountNotFoundException, UserNotFoundException, AccountNotEnoughMoney {
		// Check if account and user exist
		Account account = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

		// Credit the user's account
		account.withdrawMoney(amount);

		// Save transfer in bank transfer table
		BankTransfer bankTransfer = new BankTransfer(-amount); // negative because withdrawal
		user.addBankTransfer(bankTransfer);
		bankTransferRepository.save(bankTransfer);

		// Save user's account with the new balance
		return accountRepository.save(account);
	}

}
