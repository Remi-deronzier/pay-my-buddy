package deronzier.remi.payMyBuddyV2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Transaction;

public interface TransactionService {

//	Page<Transaction> findAllSentTransactions(int senderId, Pageable pageable);

	Page<Transaction> findAllSentAndReceivedTransactions(int ownerId, Pageable pageable);

	Transaction makeTransaction(int senderId, int receiverId, double amount, String description)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoney,
			TransactionSameAccountException;

}
