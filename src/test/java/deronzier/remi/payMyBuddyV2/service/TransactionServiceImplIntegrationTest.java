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
import deronzier.remi.payMyBuddyV2.exception.NegativeAmountException;
import deronzier.remi.payMyBuddyV2.exception.TransactionSameAccountException;
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.TransactionRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.service.impl.TransactionServiceImpl;

@SpringBootTest
public class TransactionServiceImplIntegrationTest {

	@Autowired
	private TransactionServiceImpl transactionService;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AccountRepository accountRepository;

	final static int senderId = 1;
	final static int receiverId = 2;
	final static double initialBalance = 100;

	User sender;
	User receiver;
	Account senderAccount;
	Account receiverAccount;

	@BeforeEach
	public void setUp() {
		Optional<Account> optionalSenderAccount = Optional.of(new Account());
		senderAccount = optionalSenderAccount.orElse(new Account());
		senderAccount.setBalance(initialBalance);

		Optional<Account> optionalReceiverAccount = Optional.of(new Account());
		receiverAccount = optionalReceiverAccount.orElse(new Account());
		receiverAccount.setBalance(initialBalance);

		Optional<User> optionalSender = Optional.of(new User());
		sender = optionalSender.orElse(new User());
		sender.setAccount(senderAccount);

		Optional<User> optionalReceiver = Optional.of(new User());
		receiver = optionalReceiver.orElse(new User());
		receiver.setAccount(receiverAccount);

		Mockito.when(userRepository.findById(senderId)).thenReturn(optionalSender);
		Mockito.when(userRepository.findById(receiverId)).thenReturn(optionalReceiver);

		Mockito.when(accountRepository.findByUserId(senderId)).thenReturn(optionalSenderAccount);
		Mockito.when(accountRepository.findByUserId(receiverId)).thenReturn(optionalReceiverAccount);
	}

	@Test
	public void given2Users_whenMakeTransaction_thenSuccess() throws AccountNotFoundException, NegativeAmountException,
			AccountNotEnoughMoneyException, TransactionSameAccountException, UserNotFoundException {
		double transactionAmount = 20;
		String transactionDescription = "transaction description";

		transactionService.makeTransaction(senderId, receiverId, transactionAmount,
				transactionDescription);

		assertThat(receiverAccount.getBalance()).isEqualTo(initialBalance + transactionAmount);
		assertThat(senderAccount.getBalance()).isEqualTo(initialBalance - transactionAmount);
	}

}
