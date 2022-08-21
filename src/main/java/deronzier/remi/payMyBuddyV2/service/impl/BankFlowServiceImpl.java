package deronzier.remi.paymybuddyv2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.paymybuddyv2.model.BankFlow;
import deronzier.remi.paymybuddyv2.repository.BankFlowRepository;
import deronzier.remi.paymybuddyv2.service.BankFlowService;

@Service
@Transactional
public class BankFlowServiceImpl implements BankFlowService {

	@Autowired
	private BankFlowRepository bankFlowRepository;

	@Override
	public Page<BankFlow> findAllSentAndReceivedBankFlowsForSpecificUser(int userId, Pageable pageable) {
		return bankFlowRepository.findBySenderIdOrReceiverId(userId, userId, pageable);
	}

	@Override
	public Page<BankFlow> findAll(Pageable pageable) {
		return bankFlowRepository.findAll(pageable);
	}

}
