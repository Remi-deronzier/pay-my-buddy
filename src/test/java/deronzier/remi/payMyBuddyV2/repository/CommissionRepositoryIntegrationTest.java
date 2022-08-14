package deronzier.remi.payMyBuddyV2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.payMyBuddyV2.model.Commission;
import deronzier.remi.payMyBuddyV2.setup.TestSetUp;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommissionRepositoryIntegrationTest {

	@Autowired
	CommissionRepository commissionRepository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void givenNewComission_whenSave_thenSuccess() {
		Commission newCommission = new Commission();
		newCommission.setAmount(TestSetUp.BANK_FLOW_AMOUNT);
		Commission insertedCommission = commissionRepository.save(newCommission);

		assertThat(entityManager.find(Commission.class, insertedCommission.getId())).isEqualTo(newCommission);
	}
}
