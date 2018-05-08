package com.green.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages={"com.green.health.user.*"})
@EnableJpaRepositories("com.green.health.user.*")
@EntityScan( basePackages = {"com.green.health.user.*"} )
public class GreenHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenHealthApplication.class, args);
	}
}