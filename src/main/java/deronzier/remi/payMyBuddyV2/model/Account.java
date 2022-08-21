package deronzier.remi.paymybuddyv2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import lombok.Data;

@Data
@Entity
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, columnDefinition = "double default 0")
	private double balance;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Transient
	private boolean balanceNegative;

	public boolean getBalanceNegative() {
		return balance < 0;
	}

	public void addMoney(double amount) throws NegativeAmountException {
		if (amount <= 0) {
			throw new NegativeAmountException("Amount must be strictly positive");
		}
		balance += amount;
	}

	public void withdrawMoney(double amount, boolean shouldForceWithdrawal)
			throws NegativeAmountException, AccountNotEnoughMoneyException {
		if (shouldForceWithdrawal) {
			balance -= amount;
		} else {
			if (amount <= 0) {
				throw new NegativeAmountException("Amount must be strictly positive");
			}
			if (balance - amount < 0) {
				throw new AccountNotEnoughMoneyException("The account has not enough money");
			}
			balance -= amount;
		}
	}

}
