package deronzier.remi.payMyBuddyV2.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.setup.TestSetUp;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BankFlowRepositoryIntegrationTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BankFlowRepository bankFlowRepository;

	@BeforeEach
	void setUp() throws UserNotFoundException {
		User user1 = userRepository.findById(TestSetUp.USER1_ID).orElseThrow(UserNotFoundException::new);
		User user2 = userRepository.findById(TestSetUp.USER2_ID).orElseThrow(UserNotFoundException::new);
		TestSetUp.setUpBankFlowsData(user1, user2);
		for (BankFlow bankFlow : TestSetUp.BANK_FLOWS) {
			entityManager.persist(bankFlow);
		}
	}

	@Test
	void givenBankFlowsCreated_whenFindByTimeStampBetween_thenSuccess() throws UserNotFoundException {
		List<BankFlow> retrievedBankFlowsList = new ArrayList<>();

		Iterable<BankFlow> retrievedBankFlows = bankFlowRepository.findByTimeStampGreaterThanEqualAndTimeStampLessThan(
				TestSetUp.TIME_STAMP_START,
				TestSetUp.TIME_STAMP_END);
		retrievedBankFlows.forEach(retrievedBankFlowsList::add);
		assertThat(retrievedBankFlowsList, hasSize(6));
		assertThat(retrievedBankFlowsList,
				containsInAnyOrder(TestSetUp.BANK_FLOWS.get(0), TestSetUp.BANK_FLOWS.get(2),
						TestSetUp.BANK_FLOWS.get(3), TestSetUp.BANK_FLOWS.get(5),
						TestSetUp.BANK_FLOWS.get(6), TestSetUp.BANK_FLOWS.get(7)));
	}

	@Test
	void givenBankFlowsCreated_whenFindByTimeStampBetweenAndSenderId_thenSuccess() {
		List<BankFlow> retrievedBankFlowsList = new ArrayList<>();

		Iterable<BankFlow> retrievedBankFlows = bankFlowRepository
				.findByTimeStampGreaterThanEqualAndTimeStampLessThanAndSenderId(
						TestSetUp.TIME_STAMP_START,
						TestSetUp.TIME_STAMP_END, TestSetUp.USER1_ID);
		retrievedBankFlows.forEach(retrievedBankFlowsList::add);
		System.out.println(retrievedBankFlowsList.size());
		assertThat(retrievedBankFlowsList, hasSize(5));
	}

}
