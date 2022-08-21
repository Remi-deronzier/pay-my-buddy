package deronzier.remi.paymybuddyv2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import deronzier.remi.paymybuddyv2.exception.TransactionSameAccountException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.Transaction;

public interface TransactionService {

	Page<Transaction> findAllSentAndReceivedTransactionsForSpecificUser(int ownerId, Pageable pageable);

	Transaction makeTransaction(int senderId, int receiverId, double amount, String description)
			throws AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException,
			TransactionSameAccountException, UserNotFoundException;

}
