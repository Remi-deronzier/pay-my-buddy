package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.payMyBuddyV2.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
