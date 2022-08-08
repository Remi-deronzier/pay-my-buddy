package deronzier.remi.payMyBuddyV2.service.impl;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import deronzier.remi.payMyBuddyV2.service.BankTransferService;

@Service
@Transactional
public class BankTransferServiceImpl implements BankTransferService {

	@Autowired
	private BankTransferRepository bankTransferRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Page<BankTransfer> findAllByUserId(int userId, Pageable pageable) {
		return bankTransferRepository.findByUserId(userId, pageable);
	}

	@Override
	public BankTransfer makeBankTransfer(double amount, int userId, boolean isTopUp)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoney {
		BankTransfer bankTransfer;
		// Get user
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		// Get user's account
		Account userAccount = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		// Debit or credit user's account
		if (isTopUp) { // Top up
			userAccount.addMoney(amount);
			bankTransfer = new BankTransfer(amount);
		} else { // Use
			userAccount.withdrawMoney(amount);
			bankTransfer = new BankTransfer(-amount);
		}

		// Save data in DB
		user.addBankTransfer(bankTransfer);

		return bankTransferRepository.save(bankTransfer);
	}

}
