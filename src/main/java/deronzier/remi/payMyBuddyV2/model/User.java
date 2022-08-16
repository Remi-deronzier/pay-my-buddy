package deronzier.remi.payMyBuddyV2.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import deronzier.remi.payMyBuddyV2.exception.IllegalPhoneNumberException;
import deronzier.remi.payMyBuddyV2.exception.UserUnderEighteenException;
import deronzier.remi.payMyBuddyV2.validation.PasswordMatches;
import lombok.Data;

@Data
@Entity
@DynamicUpdate
@PasswordMatches
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Email cannot be null")
	@Email
	private String email;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "User name cannot be null")
	private String userName;

	@Column(nullable = false)
	@NotBlank(message = "First name cannot be null")
	private String firstName;

	@Column(nullable = false)
	@NotBlank(message = "Last name cannot be null")
	private String lastName;

	@Column(nullable = false)
	@NotBlank(message = "Password cannot be null")
	private String password;

	@Transient
	@NotBlank(message = "Password confirmation is required")
	private String passwordConfirmation;

	private Calendar created = Calendar.getInstance();

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dateOfBirth;

	@Transient
	private int age;

	private String description;

	private String phoneNumber;

	@Column(nullable = false, columnDefinition = "varchar(32) default 'AWAY'")
	@Enumerated(value = EnumType.STRING)
	private UserStatus status = UserStatus.AWAY;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean active;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
	private Account account;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<ExternalAccount> externalAccounts = new ArrayList<>();

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

	public Integer getAge() {
		return calculateAge(dateOfBirth);
	}

	public void setDateOfBirth(LocalDate dateOfBirth) throws UserUnderEighteenException {
		if (calculateAge(dateOfBirth) != null) {
			if (calculateAge(dateOfBirth) >= 18) {
				this.dateOfBirth = dateOfBirth;
			} else {
				throw new UserUnderEighteenException("User is under 18");
			}
		}
	}

	public void setPhoneNumber(String phoneNumber) throws IllegalPhoneNumberException {
		if (phoneNumber != null && !phoneNumber.isEmpty()) {
			Pattern pattern = Pattern.compile("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$");
			Matcher matcher = pattern.matcher(phoneNumber);
			if (matcher.matches()) {
				this.phoneNumber = phoneNumber;
			} else {
				throw new IllegalPhoneNumberException("Phone number is not valid");
			}
		}
	}

	public void addConnection(User user) {
		connections.add(user);
	}

	public void removeConnection(User user) {
		connections.remove(user);
	}

	public void addExternalAccount(ExternalAccount externalAccount) {
		externalAccounts.add(externalAccount);
		externalAccount.setUser(this);
	}

	public void addAcount(Account account) {
		this.account = account;
		account.setUser(this);
	}

	public Integer calculateAge(LocalDate dob) {
		LocalDate curDate = LocalDate.now();
		if ((dob != null) && (curDate != null)) {
			return Period.between(dob, curDate).getYears();
		} else {
			return null;
		}
	}

}
