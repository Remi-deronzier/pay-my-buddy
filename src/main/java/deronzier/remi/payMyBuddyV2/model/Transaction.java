package deronzier.remi.payMyBuddyV2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import deronzier.remi.payMyBuddyV2.utils.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Constants.TRANSACTION_DISCRIMINATOR)
public class Transaction extends BankFlow {

	private String description;

}
