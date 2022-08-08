package deronzier.remi.payMyBuddyV2.service.impl;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.TransactionRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.TransactionService;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	public Page<Transaction> findAllBySenderId(int senderId, Pageable pageable) {
		return transactionRepository.findBySenderId(senderId, pageable);
	}

	public Transaction makeTransaction(int senderId, int receiverId, double amount, String description)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoney,
			TransactionSameAccountException {
		// Check senderId is different from receiverId
		if (senderId == receiverId) {
			throw new TransactionSameAccountException(
					"Sender and Receiver are not different. It is not possible to make transfers to oneself");
		}

		// Get sender and receiver
		User sender = userRepository.findById(senderId)
				.orElseThrow(() -> new UserNotFoundException("Sender not found"));
		User receiver = userRepository.findById(receiverId)
				.orElseThrow(() -> new UserNotFoundException("Receiver not found"));

		// Get sender and receiver accounts
		Account senderAccount = accountRepository.findByUserId(senderId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		Account receiverAccount = accountRepository.findByUserId(receiverId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		// Debit the sender
		senderAccount.withdrawMoney(amount);

		// Credit the receiver
		receiverAccount.addMoney(amount);

		// Update sent and received transactions for the sender and the receiver
		Transaction transaction = new Transaction(description, amount);
		sender.addSentTransaction(transaction);
		receiver.addReceivedTransaction(transaction);

		return transactionRepository.save(transaction);
	}

}
