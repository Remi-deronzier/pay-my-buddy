package deronzier.remi.payMyBuddyV2.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class BankTransfer {

	public BankTransfer(double amount, ExternalAccount externalAccount) {
		this.amount = amount;
		this.externalAccount = externalAccount;
	}

	public BankTransfer() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	private double amount;

	@Column(nullable = false, updatable = false)
	private final Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinColumn(name = "external_account_id")
	private ExternalAccount externalAccount;

	@Override
	public String toString() {
		return "BankTransfer [id=" + id + ", amount=" + amount + ", timeStamp=" + timeStamp + "]";
	}

}
