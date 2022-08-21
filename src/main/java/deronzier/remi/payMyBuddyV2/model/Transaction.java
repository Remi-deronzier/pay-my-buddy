package deronzier.remi.paymybuddyv2.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import deronzier.remi.paymybuddyv2.utils.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(Constants.TRANSACTION_DISCRIMINATOR)
public class Transaction extends BankFlow {

	private String description;

}
