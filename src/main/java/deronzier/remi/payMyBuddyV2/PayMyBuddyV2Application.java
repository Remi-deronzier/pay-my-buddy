package deronzier.remi.payMyBuddyV2;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.impl.AccountServiceImpl;
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

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyV2Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Optional<User> user1 = userService.findById(1);
		LOG.info("User by id 1:\n{}", user1);
		Optional<User> user4 = userService.findById(4);
		LOG.info("User by id 4:\n{}", user4);

		userService.addConnection(1, 4);
		userService.addConnection(1, 3);
		LOG.info("User by id 1 after adding user4 and user3 to user1's contacts:\n{}", user1);

		Account account1 = accountService.addMoney(30, 1);
		LOG.info("Account of user1 after adding 30€ and before 10€:\n{}", account1);
//		Account account2 = accountService.addMoney(-30, 1);
		LOG.info("User by id 1 after adding 30€ and before 10€:\n{}", user1);

		Transaction transaction = transactionService.makeATransaction(1, 4, 10.2, null);
//		transactionService.makeATransaction(1, 1, 30, null);
		LOG.info("Transaction between user1 and user2:\n{}", transaction);
		LOG.info("User by id 1 after transaction:\n{}", user1);
		LOG.info("User by id 4 after transaction:\n{}", user4);

		accountService.withdrawMoney(20, 1);
//		accountService.withdrawMoney(40, 1);
		LOG.info("User by id 1 after withdraw money:\n{}", user1);
		LOG.info("Account of user1 after adding 30€ and before 10€:\n{}", account1);

		Pageable pageable = PageRequest.of(0, 2, Sort.by("timeStamp").descending());
		Page<Transaction> transactionsSender1Page1SortedByDateDsc = transactionService.findAllBySenderId(1, pageable);
		LOG.info("Page 1 of transactions sender1 Sorted by date in Descending Order:");
		transactionsSender1Page1SortedByDateDsc.forEach(transactionSender1 -> LOG.info(transactionSender1.toString()));

		Page<User> connectionsUser1Page1 = new PageImpl<User>(user1.get().getConnections());
		LOG.info("Page 1 of connections of user 1:");
		connectionsUser1Page1.forEach(connection -> LOG.info(connection.toString()));

	}

}
