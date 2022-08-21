package deronzier.remi.paymybuddyv2.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.Commission;

public interface CommissionService {

	double monetization() throws AccountNotFoundException, UserNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException;

	Page<Commission> findAll(Pageable pageable);

}
