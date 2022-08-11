package deronzier.remi.payMyBuddyV2.service;

import java.util.Optional;

import deronzier.remi.payMyBuddyV2.model.Account;

public interface AccountService {

	Optional<Account> findByUserId(int userId);

}
