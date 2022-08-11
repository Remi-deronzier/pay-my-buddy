package deronzier.remi.payMyBuddyV2.model;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("bank_transfer")
public class BankTransfer extends BankFlow {

	@ManyToOne
	@JoinColumn(name = "external_account_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ExternalAccount externalAccount;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private BankTransferType bankTransferType;

}
