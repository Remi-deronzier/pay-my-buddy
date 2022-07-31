package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import deronzier.remi.payMyBuddyV2.model.User;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}
