package deronzier.remi.paymybuddyv2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.paymybuddyv2.model.BankTransfer;

@Repository
public interface BankTransferRepository extends PagingAndSortingRepository<BankTransfer, Integer> {
	Page<BankTransfer> findBySenderId(int userId, Pageable pageabe);

	Iterable<BankTransfer> findByExternalAccountId(int externalAccountId);
}
