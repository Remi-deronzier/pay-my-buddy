package deronzier.remi.payMyBuddyV2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.setup.TestSetUp;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryIntegrationTest {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

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
