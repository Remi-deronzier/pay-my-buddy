package deronzier.remi.payMyBuddyV2.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;

import lombok.Data;

@Entity
@Data
public class BankTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "Id must be positive")
	private int id;

	@Column(nullable = false, updatable = false)
	@Positive(message = "Amount must be positive")
	private double amount;

	@Column(nullable = false, updatable = false)
	private Timestamp timeStamp;

}
