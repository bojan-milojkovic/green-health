package com.green.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.green.health.*"})
public class GreenHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenHealthApplication.class, args);
	}
}