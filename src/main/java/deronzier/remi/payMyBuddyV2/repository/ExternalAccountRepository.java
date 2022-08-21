package deronzier.remi.paymybuddyv2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.paymybuddyv2.model.ExternalAccount;

@Repository
public interface ExternalAccountRepository extends CrudRepository<ExternalAccount, Integer> {
}
