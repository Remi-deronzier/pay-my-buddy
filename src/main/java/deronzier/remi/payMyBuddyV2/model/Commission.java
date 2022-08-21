package deronzier.remi.paymybuddyv2.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
@Entity
public class Commission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, updatable = false)
	@Positive
	private double amount;

	@Column(nullable = false, updatable = false)
	private LocalDate date = LocalDate.now();

}
