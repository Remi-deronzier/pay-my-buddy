package deronzier.remi.paymybuddyv2.model;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import deronzier.remi.paymybuddyv2.utils.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Constants.BANK_TRANSFER_DISCRIMINATOR)
public class BankTransfer extends BankFlow {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "external_account_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ExternalAccount externalAccount;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private BankTransferType bankTransferType;

}
