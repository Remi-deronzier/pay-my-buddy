package deronzier.remi.payMyBuddyV2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	@NotBlank(message = "Email cannot be null")
	private String email;

	@Column(nullable = false)
	@NotBlank(message = "Password cannot be null")
	private String password;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean active;

	@OneToOne(mappedBy = "user")
	Account account;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "user")
	List<BankTransfer> bankTransfers = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "sender")
	List<Transaction> sentTransactions = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	List<Transaction> receivedTransactions = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false, columnDefinition = "integer default 1"))
	private List<Role> roles = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "connection", joinColumns = @JoinColumn(name = "owner_id"), inverseJoinColumns = @JoinColumn(name = "connection_id"))
	private List<User> connections = new ArrayList<>();

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

	public void addSentTransaction(Transaction transaction) {
		sentTransactions.add(transaction);
		transaction.setSender(this);
	}

	public void addReceivedTransaction(Transaction transaction) {
		receivedTransactions.add(transaction);
	}

	private String connectionsToString() {
		String res = "[";
		for (int i = 0; i < connections.size(); i++) {
			res = res + "User(id=" + connections.get(i).id + ", email=" + connections.get(i).email + ")";
			if (i < connections.size() - 1) {
				res += ", ";
			}
		}
		res += "]";
		return res;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", active=" + active + ", account="
				+ account + ", bankTransfers=" + bankTransfers + ", sentTransactions=" + sentTransactions
				+ ", receivedTransactions=" + receivedTransactions + ", roles=" + roles + ", connections="
				+ connectionsToString() + "]";
	}

}
