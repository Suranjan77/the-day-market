package com.thedaymarket;

import com.thedaymarket.utils.DateUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class TheDayMarketApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(DateUtils.getTheMarketTimeZone());
	}

	public static void main(String[] args) {
		SpringApplication.run(TheDayMarketApplication.class, args);
	}

}
