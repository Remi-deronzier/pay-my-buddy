package deronzier.remi.payMyBuddyV2.validation.passwordmatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import deronzier.remi.payMyBuddyV2.model.User;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(final PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
		final User user = (User) obj;
		return user.getPassword().equals(user.getPasswordConfirmation());
	}

}
