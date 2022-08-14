package deronzier.remi.payMyBuddyV2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.setup.TestSetUp;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryIntegrationTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void givenUserAccount_whenWithdrawMoney_thenSuccess()
			throws AccountNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		Account senderAccount = accountRepository.findByUserId(TestSetUp.USER1_ID)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		senderAccount.withdrawMoney(TestSetUp.BANK_FLOW_AMOUNT, false);
		Account senderAccountAfterTransaction = accountRepository.findByUserId(TestSetUp.USER1_ID)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));

		assertThat(senderAccountAfterTransaction.getBalance())
				.isEqualTo(TestSetUp.INITIAL_BALANCE - TestSetUp.BANK_FLOW_AMOUNT);
	}

}
