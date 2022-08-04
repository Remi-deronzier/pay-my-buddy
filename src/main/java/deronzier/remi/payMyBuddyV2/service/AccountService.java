package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;

public interface AccountService {

	Optional<Account> findByUserId(int userId);

	Account addMoney(double amount, int userId)
			throws NegativeAmountException, AccountNotFoundException, UserNotFoundException;

	Account withdrawMoney(double amount, int userId)
			throws NegativeAmountException, AccountNotFoundException, UserNotFoundException, AccountNotEnoughMoney;
}
