package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.payMyBuddyV2.model.Commission;

@Repository
public interface CommissionRepository extends PagingAndSortingRepository<Commission, Integer> {

}
