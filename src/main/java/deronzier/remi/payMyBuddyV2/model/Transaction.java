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
import javax.validation.constraints.Min;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Transaction {

	public Transaction() {

	}

	public Transaction(String description, double amount) {
		this.description = description;
		this.amount = amount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	@Min(value = 10, message = "Amount must be greater or equal to 10€")
	private double amount;

	@Column(nullable = false, updatable = false)
	private final Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sender_id")
	private User sender;

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", amount=" + amount + ", timeStamp=" + timeStamp + ", description="
				+ description + "]";
	}

}
