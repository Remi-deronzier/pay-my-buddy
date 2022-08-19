package deronzier.remi.payMyBuddyV2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import deronzier.remi.payMyBuddyV2.model.BankFlow;

public interface BankFlowService {

	Page<BankFlow> findAllSentAndReceivedBankFlowsForSpecificUser(int userId, Pageable pageable);

	Page<BankFlow> findAll(Pageable pageable);

}
