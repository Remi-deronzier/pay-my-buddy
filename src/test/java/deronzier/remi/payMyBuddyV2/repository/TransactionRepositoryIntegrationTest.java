package deronzier.remi.payMyBuddyV2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;

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
	void givenNewTransaction_whenSave_thenSuccess() {
		User sender = userRepository.findById(1).get();
		User receiver = userRepository.findById(2).get();

		Transaction newTransaction = new Transaction();
		newTransaction.setAmount(30);
		newTransaction.setDescription("transaction description");
		newTransaction.setSender(sender);
		newTransaction.setReceiver(receiver);
		Transaction insertedTransaction = transactionRepository.save(newTransaction);

		assertThat(entityManager.find(Transaction.class, insertedTransaction.getId())).isEqualTo(newTransaction);
	}

}
