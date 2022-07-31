package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import deronzier.remi.payMyBuddyV2.model.User;

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	@Query(value = "select * from User as u inner join Connection as c on u.id = c.owner_id where u.id = :ownerId", nativeQuery = true)
	Page<User> findByConnectionsOwnerId(@Param("ownerId") int ownerId, Pageable pageable);
}
