package deronzier.remi.payMyBuddyV2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.model.ExternalAccount;
import deronzier.remi.payMyBuddyV2.repository.ExternalAccountRepository;
import deronzier.remi.payMyBuddyV2.service.ExternalAccountService;

@Service
@Transactional
public class ExternalAccountServiceImpl implements ExternalAccountService {

	@Autowired
	private ExternalAccountRepository externalAccountRepository;

	@Override
	public ExternalAccount create(ExternalAccount externalAccount) {
		return externalAccountRepository.save(externalAccount);
	}

}
