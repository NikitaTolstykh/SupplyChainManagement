package com.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.delivery.repository")
@EntityScan(basePackages = "com.delivery.entity")
@EnableCaching
@EnableAsync
public class LogisticAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticAppApplication.class, args);
	}

}
