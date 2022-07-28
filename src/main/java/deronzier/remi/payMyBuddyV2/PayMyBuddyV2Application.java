package deronzier.remi.payMyBuddyV2;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.AccountService;
import deronzier.remi.payMyBuddyV2.service.TransactionService;
import deronzier.remi.payMyBuddyV2.service.UserService;

@SpringBootApplication
public class PayMyBuddyV2Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(PayMyBuddyV2Application.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyV2Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Optional<User> user1 = userService.getUser(1);
		LOG.info("User by id 1:\n{}", user1);
		Optional<User> user4 = userService.getUser(4);
		LOG.info("User by id 4:\n{}", user4);

		user1 = Optional.ofNullable(userService.addConnection(1, 4));
		LOG.info("User by id 1 after adding user4 to user1's contacts:\n{}", user1);

		Account account1 = accountService.addMoney(30, 1);
		LOG.info("Account of user1 after adding 30€ and before 10€:\n{}", account1);
//		Account account2 = accountService.addMoney(-30, 1);
//		LOG.info("Account of user1 after adding -30€ and before 40€:\n{}", account2);
		Optional<User> user1UpdatedWith30MoreEuros = userService.getUser(1);
		LOG.info("User by id 1 after adding 30€ and before 10€:\n{}", user1UpdatedWith30MoreEuros);

		Transaction transaction = transactionService.makeATransaction(1, 4, 10.2, null);
		LOG.info("Transaction between user1 and user2:\n{}", transaction);
		LOG.info("User by id 1 after transaction:\n{}", user1UpdatedWith30MoreEuros);
		LOG.info("User by id 4 after transaction:\n{}", user4);
	}

}
