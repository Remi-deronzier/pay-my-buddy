package deronzier.remi.payMyBuddyV2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Commission;

public interface CommissionService {

	double monetization() throws AccountNotFoundException, UserNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException;

	Page<Commission> findAll(Pageable pageable);

}
