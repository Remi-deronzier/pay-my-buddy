package deronzier.remi.paymybuddyv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import deronzier.remi.paymybuddyv2.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);
}
