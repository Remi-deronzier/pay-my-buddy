package deronzier.remi.payMyBuddyV2.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotBelongGoodUserException;
import deronzier.remi.payMyBuddyV2.exception.ExternalAccountNotFoundException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankTransferType;
import deronzier.remi.payMyBuddyV2.model.ExternalAccount;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.BankTransferRepository;
import deronzier.remi.payMyBuddyV2.repository.ExternalAccountRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.impl.BankTransferServiceImpl;

@SpringBootTest
public class BankTransferServiceImplIntegrationTest {

	@Autowired
	private BankTransferServiceImpl bankTransferService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private ExternalAccountRepository externalAccountRepository;

	@MockBean
	private BankTransferRepository bankTransferRepository;

	final static int senderId = 1;
	final static int externalAccountId = 1;
	final static double initialBalance = 100;
	final static double bankTransferAmount = 20;

	User sender;
	Account senderAccount;
	ExternalAccount externalAccount;

	@BeforeEach
	public void setUp() {
		Optional<Account> optionalSenderAccount = Optional.of(new Account());
		senderAccount = optionalSenderAccount.orElse(new Account());
		senderAccount.setBalance(initialBalance);

		Optional<User> optionalSender = Optional.of(new User());
		sender = optionalSender.orElse(new User());
		sender.setId(senderId);
		sender.setAccount(senderAccount);

		Optional<ExternalAccount> optionalExternalAccount = Optional.of(new ExternalAccount());
		externalAccount = optionalExternalAccount.orElse(new ExternalAccount());
		externalAccount.setUser(sender);

		Mockito.when(userRepository.findById(senderId)).thenReturn(optionalSender);
		Mockito.when(accountRepository.findByUserId(senderId)).thenReturn(optionalSenderAccount);
		Mockito.when(externalAccountRepository.findById(externalAccountId)).thenReturn(optionalExternalAccount);
	}

	@Test
	public void given1UserWithAnExternalAccount_whenMakeBankTransferTopUp_thenSucces() throws AccountNotFoundException,
			UserNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		bankTransferService.makeBankTransfer(bankTransferAmount, senderId, BankTransferType.TOP_UP, externalAccountId);

		assertThat(senderAccount.getBalance()).isEqualTo(initialBalance + bankTransferAmount);
	}

	@Test
	public void given1UserWithAnExternalAccount_whenMakeBankTransferUse_thenSucces() throws AccountNotFoundException,
			UserNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException,
			ExternalAccountNotFoundException, ExternalAccountNotBelongGoodUserException {
		bankTransferService.makeBankTransfer(bankTransferAmount, senderId, BankTransferType.USE, externalAccountId);

		assertThat(senderAccount.getBalance()).isEqualTo(initialBalance - bankTransferAmount);
	}

}
