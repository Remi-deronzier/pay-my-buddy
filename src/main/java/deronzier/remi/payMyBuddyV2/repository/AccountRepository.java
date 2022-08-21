package deronzier.remi.paymybuddyv2.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.paymybuddyv2.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {
	Optional<Account> findByUserId(int userId);
}
