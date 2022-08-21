package deronzier.remi.paymybuddyv2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.paymybuddyv2.exception.ExternalAccountNotFoundException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.BankTransfer;
import deronzier.remi.paymybuddyv2.model.BankTransferType;

public interface BankTransferService {

	Page<BankTransfer> findAllBankTransfersForSpecificUser(int userId, Pageable pageable);

	Iterable<BankTransfer> findAllBankTransfersForSpecificExternalAccount(int externalAccountId);

	BankTransfer makeBankTransfer(double amount, int userId, BankTransferType bankTransferType, int externalAccountId)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException;

}
