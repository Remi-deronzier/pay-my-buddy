package deronzier.remi.payMyBuddyV2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Profile("!test")
@Configuration
@EnableScheduling
public class GeneralConfig {
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

}
