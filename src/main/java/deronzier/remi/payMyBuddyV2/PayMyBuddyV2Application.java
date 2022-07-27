package deronzier.remi.payMyBuddyV2;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.service.UserService;

@SpringBootApplication
public class PayMyBuddyV2Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(PayMyBuddyV2Application.class);

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyV2Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Optional<User> user1 = userService.getUser(1);
		LOG.info("Task by id 1:\n{}", user1);
	}

}
