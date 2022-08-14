package deronzier.remi.payMyBuddyV2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PayMyBuddyV2Application {

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyV2Application.class, args);
	}

}
