package deronzier.remi.paymybuddyv2.service;

import deronzier.remi.paymybuddyv2.model.ExternalAccount;

public interface ExternalAccountService {

	ExternalAccount save(ExternalAccount externalAccount);

	void delete(int externalAccountId);

}
