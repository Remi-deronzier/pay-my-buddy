package deronzier.remi.paymybuddyv2.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.paymybuddyv2.model.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUserName(String userName);
}
