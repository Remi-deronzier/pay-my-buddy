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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Positive(message = "Id must be positive")
	private int id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Email cannot be null")
	private String email;

	@Column(nullable = false)
	@NotBlank(message = "Password cannot be null")
	private String password;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean active;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	List<BankTransfer> bankTransfers = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	List<Transaction> sentTransactions = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", active=" + active
				+ ", bankTransfers=" + bankTransfers + ", sentTransactions=" + sentTransactions
				+ ", receivedTransactions=" + receivedTransactions + ", roles=" + roles + "]";
	}

}
