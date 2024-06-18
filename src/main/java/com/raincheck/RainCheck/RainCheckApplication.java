package com.raincheck.RainCheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RainCheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(RainCheckApplication.class, args);
	}

}
