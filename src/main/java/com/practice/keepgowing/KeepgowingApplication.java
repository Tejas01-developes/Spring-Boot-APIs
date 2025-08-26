package com.practice.keepgowing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class KeepgowingApplication {

	public static void main(String[] args) {

		SpringApplication.run(KeepgowingApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordencoder(){
		return new BCryptPasswordEncoder();
	}
}
