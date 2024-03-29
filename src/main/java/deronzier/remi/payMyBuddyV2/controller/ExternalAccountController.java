package deronzier.remi.paymybuddyv2.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.ExternalAccount;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.security.CustomUser;
import deronzier.remi.paymybuddyv2.service.ExternalAccountService;
import deronzier.remi.paymybuddyv2.service.UserService;

@Controller
@RequestMapping(value = "/externalAccounts")
public class ExternalAccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private ExternalAccountService externalAccountService;

	@GetMapping
	public String getExternalAccounts(Model model, @AuthenticationPrincipal CustomUser customUser)
			throws UserNotFoundException {
		final int userId = customUser.getId();
		User user = userService.findUserById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		List<ExternalAccount> externalAccounts = user.getExternalAccounts();
		model.addAttribute("externalAccounts", externalAccounts);
		return "external-accounts/view";
	}

	@GetMapping("/add")
	public String getAddExternalAccount(Model model, HttpServletRequest request) {
		// Check validation form server side
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			String labelExternalAccountError = (String) inputFlashMap.get("labelExternalAccountError");
			DataIntegrityViolationException dataIntegrityViolationException = (DataIntegrityViolationException) inputFlashMap
					.get("dataIntegrityViolationException");
			model.addAttribute("labelExternalAccountError", labelExternalAccountError);
			model.addAttribute("dataIntegrityViolationException", dataIntegrityViolationException);
		}

		ExternalAccount newExternalAccount = new ExternalAccount();
		model.addAttribute("newExternalAccount", newExternalAccount);
		return "external-accounts/add";
	}

	@PostMapping("/add")
	public String postAddExternalAccount(@AuthenticationPrincipal CustomUser customUser,
			@Valid @ModelAttribute("newExternalAccount") ExternalAccount newExternalAccount,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
			throws UserNotFoundException {
		final int userId = customUser.getId();

		// Check potential form errors
		if (bindingResult.hasErrors()) { // if label of external account is blank
			redirectAttributes.addFlashAttribute("labelExternalAccountError",
					bindingResult.getFieldError("label").getDefaultMessage());
			return "redirect:/externalAccounts/add?isNewExternalAccountAddedSuccessfully=false";
		}

		try {
			User user = userService.findUserById(userId)
					.orElseThrow(() -> new UserNotFoundException("User not found"));
			user.addExternalAccount(newExternalAccount);
			userService.save(user);
			return "redirect:/externalAccounts?isNewExternalAccountAddedSuccessfully=true";
		} catch (DataIntegrityViolationException dive) {
			redirectAttributes.addFlashAttribute("dataIntegrityViolationException", dive);
			return "redirect:/externalAccounts/add?isNewExternalAccountAddedSuccessfully=false";
		}
	}

	@DeleteMapping("/delete/{externalAccountId}")
	public String deleteExternalAccount(@PathVariable final int externalAccountId) {
		externalAccountService.delete(externalAccountId);
		return "redirect:/externalAccounts?isExternalAccountDeletedSuccessfully=true";
	}

}
