package deronzier.remi.payMyBuddyV2.service;

import deronzier.remi.payMyBuddyV2.model.ExternalAccount;

public interface ExternalAccountService {

	ExternalAccount save(ExternalAccount externalAccount);

	void delete(int externalAccountId);

}