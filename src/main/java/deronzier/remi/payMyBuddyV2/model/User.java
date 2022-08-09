package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Email cannot be null")
	private String email;

	@Column(nullable = false)
	@NotBlank(message = "Password cannot be null")
	private String password;

	@Past
	private LocalDate dateOfBirth;

	@Transient
	private int age;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean active;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
	private Account account;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<ExternalAccount> externalAccounts = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<BankTransfer> bankTransfers = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sender")
	private List<Transaction> sentTransactions = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "receiver")
	@JoinColumn(name = "receiver_id")
	private List<Transaction> receivedTransactions = new ArrayList<>();

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, columnDefinition = "integer default 1"))
	private List<Role> roles = new ArrayList<>();

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "connection", joinColumns = @JoinColumn(name = "owner_id"), inverseJoinColumns = @JoinColumn(name = "connection_id"))
	private List<User> connections = new ArrayList<>();

	public int getAge() {
		return calculateAge(dateOfBirth);
	}

	public void addConnection(User user) {
		connections.add(user);
	}

	public void removeConnection(User user) {
		connections.remove(user);
	}

	public void addBankTransfer(BankTransfer bankTransfer) {
		bankTransfers.add(bankTransfer);
		bankTransfer.setUser(this);
	}

	public void addExternalAccount(ExternalAccount externalAccount) {
		externalAccounts.add(externalAccount);
		externalAccount.setUser(this);
	}

	public void addSentTransaction(Transaction transaction) {
		sentTransactions.add(transaction);
		transaction.setSender(this);
	}

	public void addReceivedTransaction(Transaction transaction) {
		receivedTransactions.add(transaction);
		transaction.setReceiver(this);
	}

	public void addAcount(Account account) {
		this.account = account;
		account.setUser(this);
	}

	public int calculateAge(LocalDate dob) {
		// creating an instance of the LocalDate class and invoking the now() method
		// now() method obtains the current date from the system clock in the default
		// time zone
		LocalDate curDate = LocalDate.now();
		// calculates the amount of time between two dates and returns the years
		if ((dob != null) && (curDate != null)) {
			return Period.between(dob, curDate).getYears();
		} else {
			return 0;
		}
	}

}
