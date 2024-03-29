package deronzier.remi.paymybuddyv2.service.impl;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import deronzier.remi.paymybuddyv2.exception.TransactionSameAccountException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.Account;
import deronzier.remi.paymybuddyv2.model.Transaction;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.repository.AccountRepository;
import deronzier.remi.paymybuddyv2.repository.TransactionRepository;
import deronzier.remi.paymybuddyv2.repository.UserRepository;
import deronzier.remi.paymybuddyv2.service.TransactionService;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Transaction makeTransaction(int senderId, int receiverId, double amount, String description)
			throws AccountNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException,
			TransactionSameAccountException, UserNotFoundException {
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
		senderAccount.withdrawMoney(amount, false);

		// Credit the receiver
		receiverAccount.addMoney(amount);

		// Update sent and received transactions for the sender and the receiver
		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);
		transaction.setSender(sender);
		transaction.setReceiver(receiver);

		return transactionRepository.save(transaction);
	}

	@Override
	public Page<Transaction> findAllSentAndReceivedTransactionsForSpecificUser(int ownerId, Pageable pageable) {
		return transactionRepository.findBySenderIdOrReceiverId(ownerId, ownerId, pageable);
	}

}
