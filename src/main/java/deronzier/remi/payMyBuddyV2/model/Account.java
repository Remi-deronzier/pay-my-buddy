package deronzier.remi.payMyBuddyV2.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoney;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, columnDefinition = "double default 0")
	@NonNull
	private double balance;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public void addMoney(double amount) throws NegativeAmountException {
		if (amount <= 0) {
			throw new NegativeAmountException("Amount must be strictly positive");
		}
		balance += amount;
	}

	public void withdrawMoney(double amount) throws NegativeAmountException, AccountNotEnoughMoney {
		if (amount <= 0) {
			throw new NegativeAmountException("Amount must be strictly positive");
		}
		if (balance - amount < 0) {
			throw new AccountNotEnoughMoney("The account has not enough money");
		}
		balance -= amount;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + "]";
	}

}
