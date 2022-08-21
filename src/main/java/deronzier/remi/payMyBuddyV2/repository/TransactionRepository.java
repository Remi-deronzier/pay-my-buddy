package deronzier.remi.paymybuddyv2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.paymybuddyv2.model.Transaction;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Integer> {
	Page<Transaction> findBySenderIdOrReceiverId(int senderId, int receiverId, Pageable pageabe);
}
