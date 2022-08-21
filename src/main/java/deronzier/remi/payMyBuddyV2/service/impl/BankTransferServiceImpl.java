package deronzier.remi.paymybuddyv2.service.impl;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.paymybuddyv2.exception.ExternalAccountNotFoundException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.Account;
import deronzier.remi.paymybuddyv2.model.BankTransfer;
import deronzier.remi.paymybuddyv2.model.BankTransferType;
import deronzier.remi.paymybuddyv2.model.ExternalAccount;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.repository.AccountRepository;
import deronzier.remi.paymybuddyv2.repository.BankTransferRepository;
import deronzier.remi.paymybuddyv2.repository.ExternalAccountRepository;
import deronzier.remi.paymybuddyv2.repository.UserRepository;
import deronzier.remi.paymybuddyv2.service.BankTransferService;

@Service
@Transactional
public class BankTransferServiceImpl implements BankTransferService {

	@Autowired
	private BankTransferRepository bankTransferRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ExternalAccountRepository externalAccountRepository;

	@Override
	public Page<BankTransfer> findAllBankTransfersForSpecificUser(int userId, Pageable pageable) {
		return bankTransferRepository.findBySenderId(userId, pageable);
	}

	@Override
	public BankTransfer makeBankTransfer(double amount, int userId, BankTransferType bankTransferType,
			int externalAccountId)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		// Get user
		User sender = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		// Get user's account
		Account senderAccount = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		// Get user's external account
		ExternalAccount externalAccount = externalAccountRepository.findById(externalAccountId)
				.orElseThrow(() -> new ExternalAccountNotFoundException("External account not found"));

		// Check that the external account do belongs to the user
		if (userId != externalAccount.getUser().getId()) {
			throw new ExternalAccountNotBelongGoodUserException(
					"External account does not belong to this specific user");
		}

		// Debit or credit user's account
		BankTransfer bankTransfer = new BankTransfer();
		bankTransfer.setExternalAccount(externalAccount);
		bankTransfer.setAmount(amount);
		bankTransfer.setSender(sender);
		switch (bankTransferType) {
		case TOP_UP:
			senderAccount.addMoney(amount);
			bankTransfer.setBankTransferType(bankTransferType);
			break;
		case USE:
			senderAccount.withdrawMoney(amount, false);
			bankTransfer.setBankTransferType(bankTransferType);
		}

		// Save data in DB
		return bankTransferRepository.save(bankTransfer);
	}

	@Override
	public Iterable<BankTransfer> findAllBankTransfersForSpecificExternalAccount(int externalAccountId) {
		return bankTransferRepository.findByExternalAccountId(externalAccountId);
	}

}
