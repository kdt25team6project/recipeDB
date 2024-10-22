package com.bestprice.bestprice_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.bestprice.bestprice_back")
@EnableJpaRepositories(basePackages = "com.bestprice.bestprice_back.components.repository")
public class BestpriceBackApplication {
	public static void main(String[] args) {
		SpringApplication.run(BestpriceBackApplication.class, args);
	}
}
