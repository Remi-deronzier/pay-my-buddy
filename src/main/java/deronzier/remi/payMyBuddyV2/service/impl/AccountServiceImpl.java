package deronzier.remi.paymybuddyv2.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.paymybuddyv2.model.Account;
import deronzier.remi.paymybuddyv2.repository.AccountRepository;
import deronzier.remi.paymybuddyv2.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Optional<Account> findByUserId(int userId) {
		return accountRepository.findByUserId(userId);
	}

}
