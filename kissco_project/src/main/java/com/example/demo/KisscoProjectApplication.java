package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling	// 스케쥴러 기능
@SpringBootApplication
public class KisscoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KisscoProjectApplication.class, args);
	}

}
