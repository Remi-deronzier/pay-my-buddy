package deronzier.remi.payMyBuddyV2.validation.passwordvalid;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public boolean isValid(final String password, final ConstraintValidatorContext context) {
		List<Rule> rules = new ArrayList<>();
		// Rule 1: Password length should be in between
		// 8 and 16 characters
		rules.add(new LengthRule(8, 16));
		// Rule 2: No whitespace allowed
		rules.add(new WhitespaceRule());
		// Rule 3.a: At least one Upper-case character
		rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
		// Rule 3.b: At least one Lower-case character
		rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
		// Rule 3.c: At least one digit
		rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
		// Rule 3.d: At least one special character
		rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

		PasswordValidator validator = new PasswordValidator(rules);

		final RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}

		context.disableDefaultConstraintViolation();

		StringJoiner errorMessageJoiner = new StringJoiner(" *** ");
		for (String errorMessage : validator.getMessages(result)) {
			errorMessageJoiner.add(errorMessage);
		}

		context.buildConstraintViolationWithTemplate(errorMessageJoiner.toString()).addConstraintViolation();
		return false;
	}
}
