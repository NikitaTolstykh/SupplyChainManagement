package com.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.delivery.repository")
@EntityScan(basePackages = "com.delivery.entity")
public class LogisticAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticAppApplication.class, args);
	}

}
