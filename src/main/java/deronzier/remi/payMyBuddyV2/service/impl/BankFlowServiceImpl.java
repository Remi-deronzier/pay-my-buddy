package deronzier.remi.payMyBuddyV2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.repository.BankFlowRepository;
import deronzier.remi.payMyBuddyV2.service.BankFlowService;

@Service
@Transactional
public class BankFlowServiceImpl implements BankFlowService {

	@Autowired
	private BankFlowRepository bankFlowRepository;

	@Override
	public Page<BankFlow> findAllBankFlowsForSpecificUser(int userId, Pageable pageable) {
		return bankFlowRepository.findBySenderId(userId, pageable);
	}

	@Override
	public Page<BankFlow> findAll(Pageable pageable) {
		return bankFlowRepository.findAll(pageable);
	}

}
