package deronzier.remi.payMyBuddyV2.service;

import javax.security.auth.login.AccountNotFoundException;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;

public interface BankTransferService {

//	Page<BankTransfer> findAllByUserId(int userId, Pageable pageable);

	BankTransfer makeBankTransfer(double amount, int userId, boolean isTopUp, int externalAccountId)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoney,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException;

}
