package deronzier.remi.payMyBuddyV2.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.Account;
import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.model.User;
import deronzier.remi.payMyBuddyV2.repository.AccountRepository;
import deronzier.remi.payMyBuddyV2.repository.BankFlowRepository;
import deronzier.remi.payMyBuddyV2.repository.CommissionRepository;
import deronzier.remi.payMyBuddyV2.repository.TransactionRepository;
import deronzier.remi.payMyBuddyV2.repository.UserRepository;
import deronzier.remi.payMyBuddyV2.setup.TestSetUp;
import deronzier.remi.payMyBuddyV2.utils.Constants;

@SpringBootTest
@ActiveProfiles(Constants.TEST_PROFILE)
public class CommissionServiceIntegrationTest {

	@Autowired
	private CommissionService commissionService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private BankFlowRepository bankFlowRepository;

	@MockBean
	private CommissionRepository commissionRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	private Account user1Account;
	private Account user2Account;
	private List<BankFlow> dailyBankFlowsUser1 = new ArrayList<>();
	private List<BankFlow> dailyBankFlowsUser2 = new ArrayList<>();
	private List<BankFlow> dailyBankFlows = new ArrayList<>();

	@BeforeEach
	public void setUp() throws UserNotFoundException {
		Optional<User> optionalUser1 = Optional.of(new User());
		User user1 = optionalUser1.orElse(new User());
		user1.setId(TestSetUp.USER1_ID);

		Optional<User> optionalPayMyBuddySuperUser = Optional.of(new User());

		Optional<User> optionalUser2 = Optional.of(new User());
		User user2 = optionalUser2.orElse(new User());
		user2.setId(TestSetUp.USER2_ID);

		Optional<Account> optionalUser1Account = Optional.of(new Account());
		user1Account = optionalUser1Account.orElse(new Account());
		user1Account.setBalance(TestSetUp.INITIAL_BALANCE);
		user1Account.setUser(user1);

		Optional<Account> optionalUser2Account = Optional.of(new Account());
		user2Account = optionalUser2Account.orElse(new Account());
		user2Account.setBalance(TestSetUp.INITIAL_BALANCE);
		user2Account.setUser(user2);

		Optional<Account> optionalPayMyBuddySuperUserAccount = Optional.of(new Account());

		List<User> allUsers = new ArrayList<>();
		allUsers.add(user1);
		allUsers.add(user2);

		TestSetUp.setUpBankFlowsData(user1, user2);

		dailyBankFlows.add(TestSetUp.BANK_FLOWS.get(0));
		dailyBankFlows.add(TestSetUp.BANK_FLOWS.get(2));
		dailyBankFlows.add(TestSetUp.BANK_FLOWS.get(3));
		dailyBankFlows.add(TestSetUp.BANK_FLOWS.get(5));
		dailyBankFlows.add(TestSetUp.BANK_FLOWS.get(6));
		dailyBankFlows.add(TestSetUp.BANK_FLOWS.get(4));

		dailyBankFlowsUser1.add(TestSetUp.BANK_FLOWS.get(0));
		dailyBankFlowsUser1.add(TestSetUp.BANK_FLOWS.get(2));
		dailyBankFlowsUser1.add(TestSetUp.BANK_FLOWS.get(3));
		dailyBankFlowsUser1.add(TestSetUp.BANK_FLOWS.get(5));
		dailyBankFlowsUser1.add(TestSetUp.BANK_FLOWS.get(7));

		dailyBankFlowsUser2.add(TestSetUp.BANK_FLOWS.get(6));
		dailyBankFlowsUser2.add(TestSetUp.BANK_FLOWS.get(4));

		final LocalDateTime now = LocalDateTime.now(); // current day for triggering the periodic task (HH:mm = 00:00)
		final LocalDateTime startPreviousDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - 1,
				0,
				0);
		final LocalDateTime endPreviousDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);

		Mockito.when(bankFlowRepository.findByTimeStampGreaterThanEqualAndTimeStampLessThan(startPreviousDay,
				endPreviousDay))
				.thenReturn(dailyBankFlows);
		Mockito.when(bankFlowRepository.findByTimeStampGreaterThanEqualAndTimeStampLessThanAndSenderId(startPreviousDay,
				endPreviousDay, TestSetUp.USER1_ID))
				.thenReturn(dailyBankFlowsUser1);
		Mockito.when(bankFlowRepository.findByTimeStampGreaterThanEqualAndTimeStampLessThanAndSenderId(startPreviousDay,
				endPreviousDay, TestSetUp.USER2_ID))
				.thenReturn(dailyBankFlowsUser2);
		Mockito.when(userRepository.findAll()).thenReturn(allUsers);
		Mockito.when(userRepository.findById(TestSetUp.USER1_ID)).thenReturn(optionalUser1);
		Mockito.when(userRepository.findById(TestSetUp.USER2_ID)).thenReturn(optionalUser2);
		Mockito.when(userRepository.findById(Constants.PAY_MY_BUDDY_SUPER_USER_ID))
				.thenReturn(optionalPayMyBuddySuperUser);
		Mockito.when(accountRepository.findByUserId(TestSetUp.USER1_ID)).thenReturn(optionalUser1Account);
		Mockito.when(accountRepository.findByUserId(TestSetUp.USER2_ID)).thenReturn(optionalUser2Account);
		Mockito.when(accountRepository.findByUserId(Constants.PAY_MY_BUDDY_SUPER_USER_ID))
				.thenReturn(optionalPayMyBuddySuperUserAccount);
	}

	@Test
	public void givenAllBankFlows_whenMonetizeForOneDay_thenSuccess() throws AccountNotFoundException,
			UserNotFoundException, NegativeAmountException, AccountNotEnoughMoneyException {
		double totalDailyCommissionAmount = commissionService.monetization();

		assertThat(user1Account.getBalance())
				.isEqualTo(TestSetUp.INITIAL_BALANCE
						- TestSetUp.BANK_FLOW_AMOUNT * dailyBankFlowsUser1.size()
								* Constants.COMMISSION_PERCENTAGE / 100);
		assertThat(user2Account.getBalance())
				.isEqualTo(TestSetUp.INITIAL_BALANCE
						- TestSetUp.BANK_FLOW_AMOUNT * dailyBankFlowsUser2.size()
								* Constants.COMMISSION_PERCENTAGE / 100);
		assertThat(totalDailyCommissionAmount).isEqualTo(TestSetUp.BANK_FLOW_AMOUNT * dailyBankFlows.size()
				* Constants.COMMISSION_PERCENTAGE / 100);

	}

}
