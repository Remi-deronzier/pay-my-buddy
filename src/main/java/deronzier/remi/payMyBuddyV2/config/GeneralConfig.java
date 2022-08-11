package deronzier.remi.payMyBuddyV2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
public class GeneralConfig {
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

}
