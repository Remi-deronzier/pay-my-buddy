package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class BankTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	private double amount;

	@Column(nullable = false, updatable = false)
	private LocalDateTime timeStamp = LocalDateTime.now();

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "external_account_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ExternalAccount externalAccount;

}
