package deronzier.remi.payMyBuddyV2.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import deronzier.remi.payMyBuddyV2.model.ExternalAccount;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.ExternalAccountServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;

@Controller
@RequestMapping(value = "/externalAccounts")
public class ExternalAccountController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ExternalAccountServiceImpl externalAccountService;

	@GetMapping
	public String getExternalAccounts(Model model) {
		User user1 = userService.findById(UserController.OWNER_USER_ID).get();
		List<ExternalAccount> externalAccounts = user1.getExternalAccounts();
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
	public String postAddExternalAccount(
			@Valid @ModelAttribute("newExternalAccount") ExternalAccount newExternalAccount,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		// Check potential form errors
		if (bindingResult.hasErrors()) { // if label of external account is blank
			redirectAttributes.addFlashAttribute("labelExternalAccountError",
					bindingResult.getFieldError("label").getDefaultMessage());
			return "redirect:/externalAccounts/add?isNewExternalAccountAddedSuccessfully=false";
		}

		try {
			User user1 = userService.findById(UserController.OWNER_USER_ID).get();
			user1.addExternalAccount(newExternalAccount);
			userService.save(user1);
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
