package deronzier.remi.payMyBuddyV2.setup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import deronzier.remi.payMyBuddyV2.exception.UserNotFoundException;
import deronzier.remi.payMyBuddyV2.model.BankFlow;
import deronzier.remi.payMyBuddyV2.model.BankTransfer;
import deronzier.remi.payMyBuddyV2.model.BankTransferType;
import deronzier.remi.payMyBuddyV2.model.Transaction;
import deronzier.remi.payMyBuddyV2.model.User;

public class TestSetUp {

	public final static List<BankFlow> BANK_FLOWS = new ArrayList<>();
	public final static double BANK_FLOW_AMOUNT = 10;

	public final static LocalDate REFERENCE_DATE = LocalDate.of(2022, 3, 23);
	public final static LocalTime REFERENCE_TIME = LocalTime.of(0, 0);

	public final static LocalDateTime TIME_STAMP_START = LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME); // (day D,
																											// hour H,
	// minute M)
	public final static LocalDateTime TIME_STAMP_END = LocalDateTime.of(REFERENCE_DATE.plusDays(1), REFERENCE_TIME); // (day
																														// D
	// + 1,
	// hour
	// H, minute M)

	public final static int USER1_ID = 2;
	public final static int USER2_ID = 3;

	public final static int EXTERNAL_ACCOUNT_ID = 1;

	public final static double INITIAL_BALANCE = 100;

	public static void setUpBankFlowsData(User user1, User user2)
			throws UserNotFoundException {
		BANK_FLOWS.clear();

		BankTransfer bankTransfer1 = new BankTransfer();
		bankTransfer1.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer1.setTimeStamp(LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME)); // (day D, hour H, minute M)
		bankTransfer1.setSender(user1);
		bankTransfer1.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer1);

		BankTransfer bankTransfer2 = new BankTransfer();
		bankTransfer2.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer2.setTimeStamp(LocalDateTime.of(REFERENCE_DATE.minusDays(1), REFERENCE_TIME)); // (day D - 1, hour
																									// H,
																									// minute
		// M)
		bankTransfer2.setSender(user2);
		bankTransfer2.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer2);

		BankTransfer bankTransfer3 = new BankTransfer();
		bankTransfer3.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer3.setTimeStamp(LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME.minusHours(1))); // (day D, hour H -
																									// 1,
																									// minute
		// M)
		bankTransfer3.setSender(user1);
		bankTransfer3.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer3);

		BankTransfer bankTransfer4 = new BankTransfer();
		bankTransfer4.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer4.setTimeStamp(LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME.minusHours(2))); // (day D, hour H -
																									// 2,
																									// minute
		// M)
		bankTransfer4.setSender(user1);
		bankTransfer4.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer4);

		BankTransfer bankTransfer5 = new BankTransfer();
		bankTransfer5.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer5.setTimeStamp(LocalDateTime.of(REFERENCE_DATE.plusDays(1), REFERENCE_TIME)); // (day D + 1, hour H,
																									// minute M)
		bankTransfer5.setSender(user2);
		bankTransfer5.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer5);

		BankTransfer bankTransfer6 = new BankTransfer();
		bankTransfer6.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer6.setTimeStamp(LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME.minusMinutes(30))); // (day D, hour
																										// H,
																										// minute M
		// - 30)
		bankTransfer6.setSender(user1);
		bankTransfer6.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer6);

		BankTransfer bankTransfer7 = new BankTransfer();
		bankTransfer7.setAmount(BANK_FLOW_AMOUNT);
		bankTransfer7.setTimeStamp(LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME.plusHours(2))); // (day D, hour H +
																									// 2,
																									// minute
		// M)
		bankTransfer7.setSender(user2);
		bankTransfer7.setBankTransferType(BankTransferType.TOP_UP);
		BANK_FLOWS.add(bankTransfer7);

		Transaction transaction1 = new Transaction();
		transaction1.setAmount(BANK_FLOW_AMOUNT);
		transaction1.setTimeStamp(LocalDateTime.of(REFERENCE_DATE, REFERENCE_TIME.plusMinutes(1))); // (day D, hour H,
																									// minute M +
		// 1)
		transaction1.setSender(user1);
		transaction1.setReceiver(user2);
		BANK_FLOWS.add(transaction1);

		Transaction transaction2 = new Transaction();
		transaction2.setAmount(BANK_FLOW_AMOUNT);
		transaction2.setTimeStamp(LocalDateTime.of(REFERENCE_DATE.plusDays(1), REFERENCE_TIME.plusMinutes(1))); // (day
																												// D
																												// + 1,
		// minute M + 1)
		transaction2.setSender(user2);
		transaction2.setReceiver(user1);
		BANK_FLOWS.add(transaction2);
	}

}
