package deronzier.remi.paymybuddyv2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.paymybuddyv2.model.Commission;
import deronzier.remi.paymybuddyv2.setup.TestSetUp;
import deronzier.remi.paymybuddyv2.utils.Constants;

@DataJpaTest
@ActiveProfiles(Constants.TEST_PROFILE)
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
