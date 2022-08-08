package deronzier.remi.payMyBuddyV2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;

public interface BankTransferService {

	Page<BankTransfer> findAllByUserId(int userId, Pageable pageable);

	BankTransfer makeBankTransfer(double amount, int userId, boolean isTopUp)
			throws UserNotFoundException, AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoney;

}
