package com.courserabatchtest;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Application {

	public static void main(String[] args) {
		System.out.println("Coming into Application");
		SpringApplication.run(Application.class, args);
		
		
		
	}

}
