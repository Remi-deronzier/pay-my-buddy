package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Min;

import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class BankFlow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	@Min(value = 10, message = "Amount must be greater or equal to 10€")
	private double amount;

	@Column(nullable = false, updatable = false)
	private LocalDateTime timeStamp = LocalDateTime.now();

}
