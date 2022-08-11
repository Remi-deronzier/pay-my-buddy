package deronzier.remi.payMyBuddyV2.model;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class BankTransfer extends BankFlow {

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "external_account_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ExternalAccount externalAccount;

}
