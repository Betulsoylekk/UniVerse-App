package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example") // Adjust to your base package

public class UniVerseApplication {
	public static void main(String[] args) {
		SpringApplication.run(UniVerseApplication.class, args);
	}

}
