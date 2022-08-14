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
import org.springframework.test.context.ActiveProfiles;

import deronzier.remi.payMyBuddyV2.exception.AccountNotEnoughMoneyException;
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.TransactionRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.impl.TransactionServiceImpl;
import deronzier.remi.payMyBuddyV2.setup.TestSetUp;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceImplIntegrationTest {

	@Autowired
	private TransactionServiceImpl transactionService;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AccountRepository accountRepository;

	private Account senderAccount;
	private Account receiverAccount;

	@BeforeEach
	public void setUp() {
		Optional<Account> optionalSenderAccount = Optional.of(new Account());
		senderAccount = optionalSenderAccount.orElse(new Account());
		senderAccount.setBalance(TestSetUp.INITIAL_BALANCE);

		Optional<Account> optionalReceiverAccount = Optional.of(new Account());
		receiverAccount = optionalReceiverAccount.orElse(new Account());
		receiverAccount.setBalance(TestSetUp.INITIAL_BALANCE);

		Optional<User> optionalSender = Optional.of(new User());
		User sender = optionalSender.orElse(new User());
		sender.setAccount(senderAccount);

		Optional<User> optionalReceiver = Optional.of(new User());
		User receiver = optionalReceiver.orElse(new User());
		receiver.setAccount(receiverAccount);

		Mockito.when(userRepository.findById(TestSetUp.USER1_ID)).thenReturn(optionalSender);
		Mockito.when(userRepository.findById(TestSetUp.USER2_ID)).thenReturn(optionalReceiver);

		Mockito.when(accountRepository.findByUserId(TestSetUp.USER1_ID)).thenReturn(optionalSenderAccount);
		Mockito.when(accountRepository.findByUserId(TestSetUp.USER2_ID)).thenReturn(optionalReceiverAccount);
	}

	@Test
	public void given2Users_whenMakeTransaction_thenSuccess() throws AccountNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException, TransactionSameAccountException, UserNotFoundException {

		transactionService.makeTransaction(TestSetUp.USER1_ID, TestSetUp.USER2_ID, TestSetUp.BANK_FLOW_AMOUNT, null);

		assertThat(receiverAccount.getBalance()).isEqualTo(TestSetUp.INITIAL_BALANCE + TestSetUp.BANK_FLOW_AMOUNT);
		assertThat(senderAccount.getBalance()).isEqualTo(TestSetUp.INITIAL_BALANCE - TestSetUp.BANK_FLOW_AMOUNT);
	}

}
