package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "bank_flow_type")
@Data
public class BankFlow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "sender_id")
	private User sender;

	@Column(nullable = false, updatable = false)
	@Min(value = 10, message = "Amount must be greater or equal to 10€")
	private double amount;

	@Column(nullable = false, updatable = false)
	private LocalDateTime timeStamp = LocalDateTime.now();

	@Transient
	public String getDiscriminatorValue() {
		DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);

		return val == null ? null : val.value();
	}

}
