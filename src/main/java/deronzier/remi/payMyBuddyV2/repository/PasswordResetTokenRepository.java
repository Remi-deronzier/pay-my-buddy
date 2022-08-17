package deronzier.remi.payMyBuddyV2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deronzier.remi.payMyBuddyV2.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	PasswordResetToken findByToken(String token);
}
