package deronzier.remi.payMyBuddyV2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;

public interface BankTransferService {

	Page<BankTransfer> findAllBankTransfersForSpecificUser(int userId, Pageable pageable);

	Iterable<BankTransfer> findAllBankTransfersForSpecificExternalAccount(int externalAccountId);

	BankTransfer makeBankTransfer(double amount, int userId, boolean isTopUp, int externalAccountId)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException;

}
