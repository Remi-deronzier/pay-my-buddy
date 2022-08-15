package deronzier.remi.payMyBuddyV2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import deronzier.remi.payMyBuddyV2.utils.Constants;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Profile("!" + Constants.TEST_PROFILE)
@Configuration
@EnableScheduling
public class PayMyBuddyV2Config {
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

}
