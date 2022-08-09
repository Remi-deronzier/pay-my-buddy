package deronzier.remi.payMyBuddyV2;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.model.ExternalAccount;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.AccountServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.BankTransferServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.ExternalAccountServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.TransactionServiceImpl;
import deronzier.remi.payMyBuddyV2.service.impl.UserServiceImpl;

@SpringBootApplication
public class PayMyBuddyV2Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(PayMyBuddyV2Application.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private AccountServiceImpl accountService;

	@Autowired
	private TransactionServiceImpl transactionService;

	@Autowired
	private BankTransferServiceImpl bankTransferService;

	@Autowired
	private ExternalAccountServiceImpl externalAccountService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyV2Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
//		User userTest = new User("test@gmail.com", "password");
//		User newUserTest = userService.create(userTest);
//		LOG.info("User test:\n{}", newUserTest);
//
//		userService.delete(5);
//
		Optional<User> user1 = userService.findById(1);
//		LOG.info("User by id 1:\n{}", user1);
		Optional<User> user4 = userService.findById(4);
//		LOG.info("User by id 4:\n{}", user4);
//
//		userService.addConnection(1, 4);
//		userService.addConnection(1, 3);
//		LOG.info("User by id 1 after adding user4 and user3 to user1's contacts:\n{}", user1);
//
//		Account account1 = accountService.addMoney(30, 1);
//		LOG.info("Account of user1 after adding 30€ and before 10€:\n{}", account1);
////		Account account2 = accountService.addMoney(-30, 1);
//		LOG.info("User by id 1 after adding 30€ and before 10€:\n{}", user1);
//
//		Transaction transaction = transactionService.makeTransaction(1, 4, 10.2, null);
////		transactionService.makeATransaction(1, 1, 30, null);
//		LOG.info("Transaction between user1 and user4:\n{}", transaction);
//		LOG.info("User by id 1 after transaction:\n{}", user1);
//		LOG.info("User by id 4 after transaction:\n{}", user4);

//		BankTransfer bankTransferUse = bankTransferService.makeBankTransfer(10, 1, false); // use
//		LOG.info("User by id 1 after bank transfer of 10€ type use:\n{}", user1);
//		LOG.info("All bank transfers:\n{}", user1.get().getBankTransfers());
//		LOG.info("Bank transfer top up of 10€:\n{}", bankTransferUse);
//		BankTransfer bankTransferTopUp = bankTransferService.makeBankTransfer(10, 1, true); // top up
//		LOG.info("User by id 1 after bank transfer of 10€ type top up:\n{}", user1);
//		LOG.info("Bank transfer top up of 10€:\n{}", bankTransferTopUp);

//		Pageable pageable = PageRequest.of(0, 4, Sort.by("timeStamp").descending());
//		Page<BankTransfer> bankTransfersUser1SortedByDateDsc = bankTransferService.findAllByUserId(1, pageable);
//		LOG.info("Page 1 of bankTransfers sender1 Sorted by date in Descending Order:");
//		bankTransfersUser1SortedByDateDsc.forEach(bankTransfer -> LOG.info(bankTransfer.toString()));

//		accountService.withdrawMoney(20, 1);
////		accountService.withdrawMoney(40, 1);
//		LOG.info("User by id 1 after withdraw money:\n{}", user1);
//		LOG.info("Account of user1 after adding 30€ and before 10€:\n{}", account1);
//
//		Pageable pageable = PageRequest.of(0, 3, Sort.by("timeStamp").descending());
//		Page<Transaction> transactionsSender1Page1SortedByDateDsc = transactionService.findAllBySenderId(1, pageable);
//		LOG.info("Page 1 of transactions sender1 Sorted by date in Descending Order:");
//		transactionsSender1Page1SortedByDateDsc.forEach(transactionSender1 -> LOG.info(transactionSender1.toString()));
//
//		Page<User> connectionsUser1Page1 = new PageImpl<User>(user1.get().getConnections());
//		LOG.info("Page 1 of connections of user 1:");
//		connectionsUser1Page1.forEach(connection -> LOG.info(connection.toString()));
//
//		userService.deleteConnection(1, 4);
////		userService.deleteConnection(5, 4);
		System.out.println("coucou");

		List<ExternalAccount> externalAccountsUser1 = user1.get().getExternalAccounts();
		LOG.info("External accounts of user 1:");
		externalAccountsUser1.forEach(externalAccount -> LOG.info(externalAccount.toString()));

		ExternalAccount newExternalAccount = new ExternalAccount();
		newExternalAccount.setLabel("Boursorama");
//		user1.get().addExternalAccount(newExternalAccount);
//		userService.save(user1.get());
		newExternalAccount.setUser(user1.get());
		externalAccountService.save(newExternalAccount);

		BankTransfer bankTransferUse = bankTransferService.makeBankTransfer(10, 1, false, 2); // use
	}

}
