package deronzier.remi.paymybuddyv2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.paymybuddyv2.model.BankTransfer;
import deronzier.remi.paymybuddyv2.model.ExternalAccount;
import deronzier.remi.paymybuddyv2.repository.ExternalAccountRepository;
import deronzier.remi.paymybuddyv2.service.ExternalAccountService;

@Service
@Transactional
public class ExternalAccountServiceImpl implements ExternalAccountService {

	@Autowired
	private ExternalAccountRepository externalAccountRepository;

	@Autowired
	private BankTransferServiceImpl bankTransferService;

	@Override
	public ExternalAccount save(ExternalAccount externalAccount) {
		return externalAccountRepository.save(externalAccount);
	}

	@Override
	public void delete(int externalAccountId) {
		Iterable<BankTransfer> bankTransfers = bankTransferService
				.findAllBankTransfersForSpecificExternalAccount(externalAccountId);
		for (BankTransfer bankTransfer : bankTransfers) {
			bankTransfer.setExternalAccount(null);
		}
		externalAccountRepository.deleteById(externalAccountId);
	}

//	public static final ExternalAccount getPayMyBuddyAccount() {
//		final ExternalAccount payMyBuddyAccount = new ExternalAccount();
//		payMyBuddyAccount.setLabel("Pay My Buddy");
//		return payMyBuddyAccount;
//	}

}
