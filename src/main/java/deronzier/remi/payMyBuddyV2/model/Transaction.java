package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	@Min(value = 10, message = "Amount must be greater or equal to 10€")
	private double amount;

	@Column(nullable = false, updatable = false)
	private LocalDateTime timeStamp = LocalDateTime.now();

	private String description;

	@ManyToOne(optional = false)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(optional = false)
	@JoinColumn(name = "receiver_id")
	private User receiver;

}
