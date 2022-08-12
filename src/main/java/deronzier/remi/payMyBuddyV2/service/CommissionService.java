package deronzier.remi.payMyBuddyV2.service;

import javax.security.auth.login.AccountNotFoundException;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;

public interface CommissionService {

	void monetization() throws AccountNotFoundException, UserNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException;

	static final double COMMISSION_PERCENTAGE = 0.5;

}
