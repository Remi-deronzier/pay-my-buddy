package deronzier.remi.paymybuddyv2.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.paymybuddyv2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.paymybuddyv2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.paymybuddyv2.exception.ExternalAccountNotFoundException;
import deronzier.remi.paymybuddyv2.exception.NegativeAmountException;
import deronzier.remi.paymybuddyv2.exception.UserNotFoundException;
import deronzier.remi.paymybuddyv2.model.Account;
import deronzier.remi.paymybuddyv2.model.BankTransferType;
import deronzier.remi.paymybuddyv2.model.ExternalAccount;
import deronzier.remi.paymybuddyv2.model.User;
import deronzier.remi.paymybuddyv2.repository.AccountRepository;
import deronzier.remi.paymybuddyv2.repository.BankTransferRepository;
import deronzier.remi.paymybuddyv2.repository.ExternalAccountRepository;
import deronzier.remi.paymybuddyv2.repository.UserRepository;
import deronzier.remi.paymybuddyv2.setup.TestSetUp;
import deronzier.remi.paymybuddyv2.utils.Constants;

@SpringBootTest
@ActiveProfiles(Constants.TEST_PROFILE)
public class BankTransferServiceIntegrationTest {

	@Autowired
	private BankTransferService bankTransferService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private ExternalAccountRepository externalAccountRepository;

	@MockBean
	private BankTransferRepository bankTransferRepository;

	private Account senderAccount;

	@BeforeEach
	public void setUp() {
		Optional<Account> optionalSenderAccount = Optional.of(new Account());
		senderAccount = optionalSenderAccount.orElse(new Account());
		senderAccount.setBalance(TestSetUp.INITIAL_BALANCE);

		Optional<User> optionalSender = Optional.of(new User());
		User sender = optionalSender.orElse(new User());
		sender.setId(TestSetUp.USER1_ID);
		sender.setAccount(senderAccount);

		Optional<ExternalAccount> optionalExternalAccount = Optional.of(new ExternalAccount());
		ExternalAccount externalAccount = optionalExternalAccount.orElse(new ExternalAccount());
		externalAccount.setUser(sender);

		Mockito.when(userRepository.findById(TestSetUp.USER1_ID)).thenReturn(optionalSender);
		Mockito.when(accountRepository.findByUserId(TestSetUp.USER1_ID)).thenReturn(optionalSenderAccount);
		Mockito.when(externalAccountRepository.findById(TestSetUp.EXTERNAL_ACCOUNT_ID))
				.thenReturn(optionalExternalAccount);
	}

	@Test
	public void given1UserWithAnExternalAccount_whenMakeBankTransferTopUp_thenSucces() throws AccountNotFoundException,
			UserNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		bankTransferService.makeBankTransfer(TestSetUp.BANK_FLOW_AMOUNT, TestSetUp.USER1_ID, BankTransferType.TOP_UP,
				TestSetUp.EXTERNAL_ACCOUNT_ID);

		assertThat(senderAccount.getBalance()).isEqualTo(TestSetUp.INITIAL_BALANCE + TestSetUp.BANK_FLOW_AMOUNT);
	}

	@Test
	public void given1UserWithAnExternalAccount_whenMakeBankTransferUse_thenSucces() throws AccountNotFoundException,
			UserNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		bankTransferService.makeBankTransfer(TestSetUp.BANK_FLOW_AMOUNT, TestSetUp.USER1_ID, BankTransferType.USE,
				TestSetUp.EXTERNAL_ACCOUNT_ID);

		assertThat(senderAccount.getBalance()).isEqualTo(TestSetUp.INITIAL_BALANCE - TestSetUp.BANK_FLOW_AMOUNT);
	}

}
