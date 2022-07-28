package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.BankTransferRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;

@Service
public class AccountService {

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
		Account account = findByUserId(userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
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

}
