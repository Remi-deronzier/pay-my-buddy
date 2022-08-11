package deronzier.remi.payMyBuddyV2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BankFlow {

	private String description;

	@ManyToOne(optional = false)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(optional = false)
	@JoinColumn(name = "receiver_id")
	private User receiver;

}
