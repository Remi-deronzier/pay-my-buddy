package deronzier.remi.payMyBuddyV2.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.payMyBuddyV2.model.BankFlow;

@Repository
public interface BankFlowRepository extends PagingAndSortingRepository<BankFlow, Integer> {
	Page<BankFlow> findBySenderId(int userId, Pageable pageabe);

	Iterable<BankFlow> findByTimeStampGreaterThanEqualAndTimeStampLessThan(LocalDateTime timeStampStart,
			LocalDateTime timeStampEnd); // include timeStampStart and exclude timeStampEnd

	Iterable<BankFlow> findByTimeStampGreaterThanEqualAndTimeStampLessThanAndSenderId(LocalDateTime timeStampStart,
			LocalDateTime timeStampEnd,
			int senderId);
}