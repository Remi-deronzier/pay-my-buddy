package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.payMyBuddyV2.model.BankTransfer;

@Repository
public interface BankTransferRepository extends PagingAndSortingRepository<BankTransfer, Integer> {
//	Page<BankTransfer> findByUserId(int userId, Pageable pageabe);
}
