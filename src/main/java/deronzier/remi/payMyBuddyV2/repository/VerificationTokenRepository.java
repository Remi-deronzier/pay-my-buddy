package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import deronzier.remi.payMyBuddyV2.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);
}
