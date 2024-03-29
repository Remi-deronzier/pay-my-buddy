package deronzier.remi.paymybuddyv2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.Transaction;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.setup.TestSetUp;
import deronzier.remi.paymybuddyv2.utils.Constants;

@DataJpaTest
@ActiveProfiles(Constants.TEST_PROFILE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryIntegrationTest {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void givenNewTransaction_whenSave_thenSuccess() throws UserNotFoundException {
		User sender = userRepository.findById(TestSetUp.USER1_ID).orElseThrow(UserNotFoundException::new);
		User receiver = userRepository.findById(TestSetUp.USER2_ID).orElseThrow(UserNotFoundException::new);

		Transaction newTransaction = new Transaction();
		newTransaction.setAmount(TestSetUp.BANK_FLOW_AMOUNT);
		newTransaction.setSender(sender);
		newTransaction.setReceiver(receiver);
		Transaction insertedTransaction = transactionRepository.save(newTransaction);

		assertThat(entityManager.find(Transaction.class, insertedTransaction.getId())).isEqualTo(newTransaction);
	}

}
