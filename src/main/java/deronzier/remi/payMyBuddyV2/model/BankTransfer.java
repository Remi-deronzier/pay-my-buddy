package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;

import lombok.Data;

@Entity
@Data
public class BankTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	@Positive
	private double amount;

	@Column(nullable = false, updatable = false)
	private LocalDateTime timeStamp;
//	private final Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	}, optional = false)
	@JoinColumn(name = "external_account_id")
	private ExternalAccount externalAccount;

}
