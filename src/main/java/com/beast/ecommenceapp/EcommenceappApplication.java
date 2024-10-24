package com.beast.ecommenceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommenceappApplication {

	static String appRunning() {
		return "App is running";
	} 

	public static void main(String[] args) {
		
		SpringApplication.run(EcommenceappApplication.class, args);
		System.out.println(appRunning());
	}

}
