package deronzier.remi.paymybuddyv2.service;

import java.util.Optional;

import deronzier.remi.paymybuddyv2.model.Account;

public interface AccountService {

	Optional<Account> findByUserId(int userId);

}
